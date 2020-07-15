const path = require('path');

module.exports = {
    entry: {
        index: path.join(__dirname, 'src/main/javascript/index.jsx')
    },

    output: {
        path: path.join(__dirname, 'target/classes/static/js'),
        filename: '[name].min.js'
    },

    module: {
        rules: [{
            test: /\.(js|jsx)$/,
            exclude: /node_modules/,
            use: {
                loader: 'babel-loader',
                options: {
                    presets: ['@babel/preset-env'],
                    plugins: ['@babel/plugin-transform-runtime']
                }
            }
        }]
    }
};