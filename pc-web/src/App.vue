<template>
  <div id="app">
    <keep-alive :include="keepAliveComponents">
      <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    keepAliveComponents() {
      const routes = this.$router.options.routes || []
      const names = []
      routes.forEach(route => {
        if (route.meta && route.meta.keepAlive && route.name) {
          names.push(route.name)
        }
        if (route.meta && route.meta.keepAliveInclude) {
          names.push(...route.meta.keepAliveInclude)
        }
      })
      return names
    }
  }
}
</script>

<style>
body{
    margin: 0px;
    padding: 0px;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0px;
  padding: 0px;
}

.margin-0 {
    margin: 0;
}

.margin-lr-0 {
    margin-left: 0;
    margin-right: 0;
}

.margin-xs {
    margin: 5px;
}

.margin-sm {
    margin: 10px;
}

.margin {
    margin: 15px;
}

.margin-lg {
    margin: 20px;
}

.margin-xl {
    margin: 25px;
}

.margin-top-xs {
    margin-top: 5px;
}

.margin-top-sm {
    margin-top: 10px;
}

.margin-top-sm:hover {
    cursor: pointer;
    /*background-color: red;*/
}

.margin-top {
    margin-top: 15px;
}

.margin-top-lg {
    margin-top: 20px;
}

.margin-top-xl {
    margin-top: 25px;
}

.margin-right-xs {
    margin-right: 5px;
}

.margin-right-sm {
    margin-right: 10px;
}

.margin-right {
    margin-right: 15px;
}

.margin-right-lg {
    margin-right: 20px;
}

.margin-right-xl {
    margin-right: 25px;
}

.margin-bottom-0 {
    margin-bottom: 0px;
}

.margin-bottom-xs {
    margin-bottom: 5px;
}

.margin-bottom-sm {
    margin-bottom: 10px;
}

.margin-bottom {
    margin-bottom: 15px;
}

.margin-bottom-lg {
    margin-bottom: 20px;
}

.margin-bottom-xl {
    margin-bottom: 25px;
}

.margin-left-0 {
    margin-left: 0px;
}

.margin-left-xs {
    margin-left: 5px;
}

.margin-left-sm {
    margin-left: 10px;
}

.margin-left {
    margin-left: 15px;
}

.margin-left-lg {
    margin-left: 20px;
}

.margin-left-xl {
    margin-left: 25upx;
}

.margin-lr-xs {
    margin-left: 5px;
    margin-right: 10upx;
}

.margin-lr-sm {
    margin-left: 10px;
    margin-right: 10px;
}

.margin-lr {
    margin-left: 15px;
    margin-right: 15px;
}

.margin-lr-lg {
    margin-left: 20px;
    margin-right: 20px;
}

.margin-lr-xl {
    margin-left: 25px;
    margin-right: 25px;
}

.margin-tb-xs {
    margin-top: 5px;
    margin-bottom: 5px;
}

.margin-tb-sm {
    margin-top: 10px;
    margin-bottom: 10px;
}

.margin-tb {
    margin-top: 15px;
    margin-bottom: 15px;
}

.margin-tb-lg {
    margin-top: 20px;
    margin-bottom: 20px;
}

.margin-tb-xl {
    margin-top: 25px;
    margin-bottom: 25px;
}

.padding-0 {
    padding: 0;
}

.padding-lr-0 {
    padding-left: 0;
    padding-right: 0;
}

.padding-r-0 {
    padding-right: 0;
}

.padding-l-0 {
    padding-left: 0;
}

.padding-xs {
    padding: 5px;
}

.padding-sm {
    padding: 10px;
}

.padding {
    padding: 15px;
}

.padding-lg {
    padding: 20px;
}

.padding-xl {
    padding: 25px;
}

.padding-top-xs {
    padding-top: 5px;
}

.padding-top-sm {
    padding-top: 10px;
}

.padding-top {
    padding-top: 15px;
}

.padding-top-lg {
    padding-top: 20px;
}

.padding-top-xl {
    padding-top: 25px;
}

.padding-right-xs {
    padding-right: 5px;
}

.padding-right-sm {
    padding-right: 10px;
}

.padding-right {
    padding-right: 15px;
}

.padding-right-lg {
    padding-right: 20px;
}

.padding-right-xl {
    padding-right: 25px;
}

.padding-bottom-xs {
    padding-bottom: 5px;
}

.padding-bottom-sm {
    padding-bottom: 10px;
}

.padding-bottom {
    padding-bottom: 15px;
}

.padding-bottom-lg {
    padding-bottom: 20px;
}

.padding-bottom-xl {
    padding-bottom: 25px;
}

.padding-left-xs {
    padding-left: 5px;
}

.padding-left-sm {
    padding-left: 10px;
}

.padding-left {
    padding-left: 15px;
}

.padding-left-lg {
    padding-left: 20px;
}

.padding-left-xl {
    padding-left: 25px;
}

.padding-lr-xs {
    padding-left: 5px;
    padding-right: 5px;
}

.padding-lr-sm {
    padding-left: 10px;
    padding-right: 10px;
}

.padding-lr {
    padding-left: 15px;
    padding-right: 15px;
}

.padding-lr-lg {
    padding-left: 20px;
    padding-right: 20px;
}

.padding-lr-xl {
    padding-left: 25px;
    padding-right: 25px;
}

.padding-tb-xs {
    padding-top: 5px;
    padding-bottom: 5px;
}

.padding-tb-sm {
    padding-top: 10px;
    padding-bottom: 10px;
}

.padding-tb {
    padding-top: 15px;
    padding-bottom: 15px;
}

.padding-tb-lg {
    padding-top: 20px;
    padding-bottom: 20px;
}

.padding-tb-xl {
    padding-top: 25px;
    padding-bottom: 25px;
}

.flex {
    display: flex;
}
.flex-direction {
    flex-direction: column;
}

.flex-wrap {
    flex-wrap: wrap;
}

.justify-start {
    justify-content: flex-start;
}

.justify-end {
    justify-content: flex-end;
}

.justify-center {
    justify-content: center;
}

.justify-between {
    justify-content: space-between;
}

.justify-around {
    justify-content: space-around;
}

.text-center {
    text-align: center;
}

.white-bg {
    background-color: #ffffff;
}
.bg-white {
    background-color: #ffffff;
}
.ibox {
    clear: both;
    margin-bottom: 5px;
    margin-top: 0;
    padding: 0;
}
.ibox .ibox-title {
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    text-align: left;
}

.ibox-title {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #ffffff;
    border-color: #e7eaec;
    border-image: none;
    border-style: solid solid none;
    border-width: 2px 0 0;
    color: inherit;
    margin-bottom: 0;
    padding: 15px 15px 8px 15px;
    position: relative;
    clear: both;
}
.vc-main .ibox .ibox-content {
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
}
.ibox-content {
    background-color: #ffffff;
    color: inherit;
    padding: 15px 10px 15px 15px;
    border-color: #e7eaec;
    border-image: none;
    border-style: solid solid none;
    border-width: 1px 0;
}
.ibox-content {
    clear: both;
}
.ibox-title h5 {
    display: inline-block;
    font-size: 14px;
    margin: 0 0 7px;
    padding: 0;
    text-overflow: ellipsis;
    float: none;
}
.border-radius {
    border-radius: 10px;
}
.text-right{
    text-align: right;
}
.text-left{
    text-align: left;
}
.text-center{
    text-align: center;
}
ul {
    padding-inline-start: 0px;
}

ul li {
    list-style-type: none;
}

h2 {
    font-size: 24px;
}

.h1,
.h2,
.h3,
h1,
h2,
h3 {
    margin-top: 20px;
    margin-bottom: 10px;
}

h1,
h2,
h3,
h4,
h5,
h6 {
    font-weight: 100;
}

h3,
h4,
h5 {
    margin-top: 5px;
    font-weight: 600;
}

h3 {
    font-size: 16px;
}

.el-select{
  width: 100%;
}
.el-date-editor{  
  width: 100%;
}
.el-date-editor.el-input{
  width: 100%;
}

.list-group-border-radius {
  border-radius: 4px;
  border: 1px solid #ebeef5;
  background: #fff;
}

.list-group {
  list-style: none;
  padding: 0;
  margin: 0;
}

.list-group-item {
  padding: 12px 15px;
  cursor: pointer;
  /*border-bottom: 1px solid #ebeef5;*/
}

.list-group-item:hover {
  background-color: #f5f7fa;
}

.vc-node-selected {
  background-color: #409eff;
  color: #fff;
  font-weight: bold;
}
.vc-node-selected:hover {
    background-color: #409eff;
}
.table-desc {
    text-align: left;
    font-size: 13px;
    color: #606266;
  }
  
.vc-table-border {
    border: 1px solid #000000;
}

.vc-table-border tr td {
    border: 1px solid #000000;
}

.vc-table-border tr th {
    border: 1px solid #000000;
}
</style> 