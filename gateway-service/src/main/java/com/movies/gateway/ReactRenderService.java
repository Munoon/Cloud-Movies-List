package com.movies.gateway;

import com.movies.common.AuthorizedUser;
import com.movies.common.user.UserTo;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import lombok.SneakyThrows;
import org.graalvm.polyglot.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ReactRenderService {
    private static final String SET_SERVER_PROPERTY_JS_COMMAND = "window.serverProperties['%s'] = '%s';";
    private static final String SET_SERVER_GLOBAL_VARIABLE_JS_COMMAND = "var %s = '%s';";
    private static final String RENDER_HTML_JS_COMMAND = "window.renderServer();";
    private static final String GRAALVM_SETUP_JS_SCRIPT_FILE_NAME = "graalvm-setup.js";
    private static final String JS_SCRIPT_BASE_PATH = "static/js/";
    private static final String BASE_JS_SCRIPT = JS_SCRIPT_BASE_PATH + "shared.min.js";
    private final GraalJSScriptEngine engine = GraalJSScriptEngine.create(null,
            Context.newBuilder("js"));

    public Object render(String scriptName, String... properties) {
        return render(scriptName, properties, null);
    }

    public Object renderWithGlobalVar(String scriptName, String... globalVariables) {
        return render(scriptName, null, globalVariables);
    }

    @SneakyThrows
    public Object render(String scriptName, String[] properties, String[] globalVariables) {
        SimpleScriptContext context = new SimpleScriptContext();
        engine.eval(getFileInputStreamReader(GRAALVM_SETUP_JS_SCRIPT_FILE_NAME), context);
        addProperties(context, properties);
        addGlobalVariables(context, globalVariables);
        engine.eval(getFileInputStreamReader(BASE_JS_SCRIPT), context);
        engine.eval(getFileInputStreamReader(JS_SCRIPT_BASE_PATH + scriptName), context);
        return engine.eval(RENDER_HTML_JS_COMMAND, context);
    }

    private void addProperties(ScriptContext context, String... properties) throws ScriptException {
        addUserProperties(context);
        addCustomProperties(context, false, properties);
    }

    private void addGlobalVariables(ScriptContext context, String... globalVariables) throws ScriptException {
        addCustomProperties(context, true, globalVariables);
    }

    private void addCustomProperties(ScriptContext context, boolean variable, String... properties) throws ScriptException {
        if (properties != null) {
            for (String property : properties) {
                String[] split = property.split("=", 2);
                setProperty(split[0], split[1], context, variable);
            }
        }
    }

    private void addUserProperties(ScriptContext context) throws ScriptException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication
                && ((OAuth2Authentication) authentication).getUserAuthentication() instanceof UsernamePasswordAuthenticationToken
                && ((OAuth2Authentication) authentication).getUserAuthentication().getPrincipal() instanceof AuthorizedUser) {
            var auth = (AuthorizedUser) ((OAuth2Authentication) authentication).getUserAuthentication().getPrincipal();
            UserTo user = auth.getUserTo();
            setProperty(FrontendProperties.USER_AUTHENTICATED, Boolean.TRUE.toString(), context, false);
            setProperty(FrontendProperties.USER_ID, user.getId().toString(), context, false);
            setProperty(FrontendProperties.USER_NAME, user.getName(), context, false);
            setProperty(FrontendProperties.USER_SURNAME, user.getSurname(), context, false);
            setProperty(FrontendProperties.USER_EMAIL, user.getEmail(), context, false);
            setProperty(FrontendProperties.USER_ROLES, user.getRoles().toString(), context, false);
        } else {
            setProperty(FrontendProperties.USER_AUTHENTICATED, Boolean.FALSE.toString(), context, false);
        }
    }

    private void setProperty(String name, String value, ScriptContext context, boolean variable) throws ScriptException {
        String useScript = variable ? SET_SERVER_GLOBAL_VARIABLE_JS_COMMAND : SET_SERVER_PROPERTY_JS_COMMAND;
        String script = String.format(useScript, makeSafeValue(name), makeSafeValue(value));
        engine.eval(script, context);
    }

    private static InputStreamReader getFileInputStreamReader(String file) throws IOException {
        return new InputStreamReader(new ClassPathResource(file).getInputStream());
    }

    private static String makeSafeValue(String value) {
        return value.replaceAll("'", "\\\\'"); // example: "John's" -> "John\'s"
    }
}
