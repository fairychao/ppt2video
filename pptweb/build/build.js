'use strict'
require('./check-versions')()

process.env.NODE_ENV = 'production'

const ora = require('ora')
const rm = require('rimraf')
const path = require('path')
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')
const fs = require('fs')
const spinner = ora('building for production...')
spinner.start()

rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err
  webpack(webpackConfig, (err, stats) => {
    spinner.stop()
    if (err) throw err
    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false,
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))
      process.exit(1)
    }

    // 如果独立部署 请注释此代码
    console.log(chalk.cyan(' start replace fonts path.\n'))
    fs.readFile(`./pptweb/static/css/app.css`,'utf8',function(err,files){
      var result = files.replace(/static\/fonts\/element-icons/g, '..\/fonts\/element-icons');
      fs.writeFile(`./pptweb/static/css/app.css`, result, 'utf8', function (err) {
          if (err) return console.log(err);
      });
    })
    console.log(chalk.yellow(' replace fonts path end, if u deploy with jar place del this.\n'))
    // 如果独立部署 请注释此代码

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))

  })
})
