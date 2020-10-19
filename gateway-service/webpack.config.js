const path = require('path');

const jsFile = fileName => path.join(__dirname, 'src/main/javascript', fileName);
const dependingEntry = fileName => ({ import: jsFile(fileName), dependOn: 'shared' });

module.exports = {
    entry: {
        index: dependingEntry('index.jsx'),
        profile: dependingEntry('profile.jsx'),
        users: dependingEntry('users.jsx'),
        movie: dependingEntry('movie.jsx'),
        add_movie: dependingEntry('add_movie.jsx'),
        search: dependingEntry('search.tsx'),
        error: dependingEntry('error.jsx'),
        shared: ['react', 'react-dom', jsFile('components/Application.tsx')]
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