<template>
  <div>
    <el-upload :action="baseUrl + url"
               :file-list="fileList"
               :on-preview="handlePictureCardPreview"
               :on-remove="handleRemove"
               :on-success="handleSuccess"
               list-type="picture-card"
               ref="multiUpload"
               v-if="multiple"
    >
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-upload :action="baseUrl + url" :on-success="handleSuccess"
               :show-file-list="false"
               :style="avatarStyle"
               class="logo-uploader"
               ref="singleUpload"
               v-else>
      <div @mouseout="showBtn=false" @mouseover="showBtn=true">
        <i @click.stop="removeSingle" class="el-icon-close remove-btn" v-show="dialogImageUrl && showBtn"></i>
        <img :src="dialogImageUrl" :style="avatarStyle" v-if="dialogImageUrl">
        <i :style="avatarStyle" class="el-icon-plus logo-uploader-icon" v-else></i>
      </div>
    </el-upload>
    <v-dialog max-width="500" v-model="show">
      <img :src="dialogImageUrl" alt="" width="500px">
    </v-dialog>
  </div>
</template>

<script>
  import {Upload} from 'element-ui';
  import config from '../../config'

  export default {
    name: "vUpload",
    components: {
      elUpload: Upload
    },
    props: {
      url: {
        type: String
      },
      value: {},
      multiple: {
        type: Boolean,
        default: true
      },
      picWidth: {
        type: Number,
        default: 150
      },
      picHeight: {
        type: Number,
        default: 150
      }
    },
    data() {
      return {
        showBtn: false,
        show: false,
        dialogImageUrl: "",
        baseUrl: config.api,
        avatarStyle: {
          width: this.picWidth + 'px',
          height: this.picHeight + 'px',
          'line-height': this.picHeight + 'px'
        },
        fileList: []
      }
    },
    mounted() {
      if (!this.value || this.value.length <= 0) {
        return;
      }
      if (this.multiple) {
        this.fileList = this.value.map(f => {
          return {response: f, url: f}
        });
      } else {
        this.dialogImageUrl = this.value;
      }
    },
    methods: {
      handleSuccess(resp, file) {
        if (!this.multiple) {
          this.dialogImageUrl = file.response;
          this.$emit("input", file.response)
        } else {
          this.fileList.push(file)
          this.$emit("input", this.fileList.map(f => f.response))
        }
      },
      handleRemove(file, fileList) {
        this.fileList = fileList;
        this.$emit("input", fileList.map(f => f.response))
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.response;
        this.show = true;
      },
      removeSingle() {
        this.dialogImageUrl = "";
        this.$refs.singleUpload.clearFiles();
      }
    },
    watch: {
      value: {
        deep: true,
        handler(val, oldVal) {

          if (this.multiple) {
            this.fileList = val.map(f => {
              return {response: f, url: f}
            });
          } else {
            this.dialogImageUrl = val;
          }
        }
      }
    }
  }
</script>

<style scoped>
  .logo-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    float: left;
  }

  .logo-uploader:hover {
    border-color: #409EFF;
  }

  .logo-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    text-align: center;
  }

  .remove-btn {
    position: absolute;
    right: 0;
    font-size: 16px;
  }

  .remove-btn:hover {
    color: #c22;
  }
</style>
