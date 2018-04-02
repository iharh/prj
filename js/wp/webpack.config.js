module.exports = {
    entry: "./src/app.js",
    output: {
        path: __dirname + "/public", // need to be absolute
        filename: "bundle.js"
    },
    devServer: {
        contentBase: __dirname + "/public"
    }
}
