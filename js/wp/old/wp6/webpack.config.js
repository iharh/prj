var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    // mode: 'production',
    mode: 'development',
    entry: './src/app.js',
    output: {
        path: __dirname + "/public", // need to be absolute
        filename: 'bundle.js'
    },
    plugins: [
        new HtmlWebpackPlugin({
            hash: true,
            filename: './index.html'
        })
    ],
    devServer: {
        contentBase: __dirname + "/public/",
        port: 3000
    },
    module: {
        rules: [
            {
                test: /\.js/,
                loader: "babel-loader"
            }
        ]
    }
}
