<script setup>

console.log("stepup")
</script>

<style>
/*每个页面公共css */
::v-deep .uni-popup__wrapper{
  background-color: white !important;
  border-radius: 10px;
  overflow-y: auto;
}
/* 禁止透传 */
::v-deep uni-popup{
  z-index: 99999 !important;
}
html, body {
  touch-action: manipulation; /* 禁用双击放大 */
}
page{
  height: 100% !important;
}
::v-deep .uni-card__cover-image {
  width: 100% !important;
}
*,html,body,view,div {
  box-sizing: border-box;
}
</style>
