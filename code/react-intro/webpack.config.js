module.exports = {
    mode: "development",
    resolve: {
        extensions: ['.tsx', '.ts', '.js'],
    },
    devServer: {
        historyApiFallback: true,
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
        ],
    },
}