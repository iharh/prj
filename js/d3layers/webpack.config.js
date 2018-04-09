const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    devServer: {
        contentBase: __dirname + "/public",
        port: 3000
    },
    plugins: [
        new HtmlWebpackPlugin()
    ]
}
