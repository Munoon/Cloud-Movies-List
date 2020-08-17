const path = require('path');

const jsFile = fileName => path.join(__dirname, 'src/main/javascript', fileName);

module.exports = {
    entry: {
        index: jsFile('index.jsx'),
        profile: jsFile('profile.jsx'),
        users: jsFile('users.jsx'),
        movie: jsFile('movie.jsx'),
        add_movie: jsFile('add_movie.jsx'),
        error: jsFile('error.jsx')
    },

    output: {
        path: path.join(__dirname, 'target/classes/static/js'),
        filename: '[name].min.js'
    },

    resolve: {
        extensions: ['.js', '.jsx', '.ts', '.tsx', '.scss'],
    },

    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env'],
                        plugins: ['@babel/plugin-transform-runtime']
                    }
                }
            },
            {
                test: /\.(tsx|ts)$/,
                use: 'ts-loader',
                exclude: '/node_modules/'
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.s[ac]ss$/i,
                use: [
                    process.env.NODE_ENV !== 'production'
                        ? 'style-loader'
                        : MiniCssExtractPlugin.loader,
                    'css-loader',
                    'sass-loader',
                ]
            }
        ]
    }
};