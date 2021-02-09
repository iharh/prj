const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkTSCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');

module.exports = {
  mode: 'development',
  resolve: {
    alias: {
      '@components': path.resolve(__dirname, 'src/components/')
    },
    extensions: [".js", ".jsx", ".ts", ".tsx"]
  },
  output: {
    filename: '[name].[hash].js'
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx|ts|tsx)$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        options: {
          presets: [
            '@babel/preset-env',
            [ 'babel-preset-react-app',
              // { 'flow': false, 'typescript': true, runtime: 'automatic' } ], // https://reactjs.org/blog/2020/09/22/introducing-the-new-jsx-transform.html
              { runtime: 'automatic' } ], // https://reactjs.org/blog/2020/09/22/introducing-the-new-jsx-transform.html
            ]
        }
      }
    ]
  },
  plugins: [
    new ForkTsCheckerWebpackPlugin(
      //typescript: {
      //  diagnosticOptions: {
      //    semantic: true,
      //    syntactic: true,
      //  },
      //  mode: "write-references",
      //},
    ),
    new HtmlWebpackPlugin({
      template: "./src/index.html"
    })
  ]
}
