const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const outputFolder = path.join(__dirname, 'target/classes/static');
const cssOutputFolder = path.join(outputFolder, 'css');
const jsFile = fileName => path.join(__dirname, 'src/main/javascript', fileName);
const cssFile = fileName => jsFile(path.join('components/scss', fileName));
const dependingEntry = fileName => ({ import: jsFile(fileName), dependOn: 'js/shared.min.js' });
const dependencyEntries = entries => entries.reduce((acc, item) => {
    const fileName = item.substr(0, item.indexOf('.'));
    if (item.endsWith('.jsx') || item.endsWith('.tsx') || item.endsWith('.js')) {
        acc[`js/${fileName}.min.js`] = dependingEntry(item);
    } else if (item.endsWith('.css') || item.endsWith('.scss')) {
        acc[`css/${fileName}.min`] = cssFile(item);
    }
    return acc;
}, {});

module.exports = {
    entry: {
        ...dependencyEntries([
            'index.jsx', 'profile.jsx',
            'users.jsx', 'movie.jsx',
            'add_movie.jsx', 'search.tsx',
            'error.jsx', 'style.scss'
        ]),
        'js/shared.min.js': ['react-dom', jsFile('components/Application.tsx')]
    },

    output: {
        path: outputFolder,
        filename: '[name]'
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
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    'sass-loader',
                ]
            }
        ]
    },

    plugins: [
        new MiniCssExtractPlugin(),
        new CopyWebpackPlugin({
            patterns: [
                { from: 'node_modules/bootswatch/dist/superhero/bootstrap.min.css', to: cssOutputFolder },
                { from: 'node_modules/react-toastify/dist/ReactToastify.min.css', to: cssOutputFolder }
            ],
        }),
    ],
};