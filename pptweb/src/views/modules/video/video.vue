<template>
  <div class="video-div-expand">
    <el-form :inline="true" :model="dataForm">
      <el-form-item>
        <el-select v-model="dataForm.chnlType" clearable filterable placeholder="合成语音渠道类型">
          <el-option
            v-for="item in option.chnlType"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="dataForm.speed" clearable filterable placeholder="语速">
          <el-option
            v-for="item in option.speed"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="dataForm.pitch" clearable filterable placeholder="音调">
          <el-option
            v-for="item in option.pitch"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="dataForm.volume" clearable filterable placeholder="音量">
          <el-option
            v-for="item in option.volume"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="dataForm.voiceType" clearable filterable placeholder="语音类型">
          <el-option
            v-for="item in option.voiceType"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

    <div style="margin:30px 1000px 0px 0px">
      <el-upload ref="upload"
                 :action="getUrl('/video/upFile')"
                 :on-remove="handleRemove"
                 :limit="100"
                 :auto-upload="true"
                 :on-success="uploadSucc">
        <el-button size="small" type="primary" >上传原始文件</el-button>
      </el-upload>
      <el-button :loading="handelLoading" style="margin:50px 0 30px 0px;" type="primary" icon="el-icon-document"
                 @click="handleVideo">
        合成视频
      </el-button>
      <el-button style="margin:50px 0px 30px 20px;" type="primary" icon="el-icon-document" @click="downloadVideo()">下载合成结果</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        dataForm: {
          chnlType: '',
          speed: ''
        },
        handelLoading: false,
        fileName: '',
        downFileName: '',
        option: {
          chnlType: [
            {label: '百度', value: '1'}
          ],
          speed: [
            {label: '降5档', value: '0'},
            {label: '降4档', value: '1'},
            {label: '降3档', value: '2'},
            {label: '降2档', value: '3'},
            {label: '降1档', value: '4'},
            {label: '标准语速', value: '5'},
            {label: '升1档', value: '6'},
            {label: '升2档', value: '7'},
            {label: '升3档', value: '8'},
            {label: '升4档', value: '9'},
            {label: '升5档', value: '10'}
          ],
          pitch: [
            {label: '降5档', value: '0'},
            {label: '降4档', value: '1'},
            {label: '降3档', value: '2'},
            {label: '降2档', value: '3'},
            {label: '降1档', value: '4'},
            {label: '标准语调', value: '5'},
            {label: '升1档', value: '6'},
            {label: '升2档', value: '7'},
            {label: '升3档', value: '8'},
            {label: '升4档', value: '9'},
            {label: '升5档', value: '10'}
          ],
          volume: [
            {label: '降5档', value: '0'},
            {label: '降4档', value: '1'},
            {label: '降3档', value: '2'},
            {label: '降2档', value: '3'},
            {label: '降1档', value: '4'},
            {label: '标准语调', value: '5'},
            {label: '升1档', value: '6'},
            {label: '升2档', value: '7'},
            {label: '升3档', value: '8'},
            {label: '升4档', value: '9'},
            {label: '升5档', value: '10'},
            {label: '升6档', value: '11'},
            {label: '升7档', value: '12'},
            {label: '升8档', value: '13'},
            {label: '升9档', value: '14'},
            {label: '升10档', value: '15'}
          ],
          voiceType: [
            {label: '度小美', value: '0'},
            {label: '度小宇', value: '1'},
            {label: '度逍遥（基础）', value: '3'},
            {label: '度丫丫', value: '4'},
            {label: '度小娇', value: '5'},
            {label: '度米朵', value: '103'},
            {label: '度博文', value: '106'},
            {label: '度小童', value: '110'},
            {label: '度小萌', value: '111'},
            {label: '度逍遥（精品）', value: '5003'},
            {label: '度小鹿', value: '5118'}
          ]
        }
      }
    },
    methods: {
      handleRemove (file, fileList) {
        this.fileList = fileList
      },
      getUrl (url) {
        return this.$http.adornUrl(url)
      },
      // 文件超出个数限制时的校验
      exceedFile () {
        this.$message.error('只能上传一个文件!')
      },
      // 文件上传成功的事件
      uploadSucc (response, file, fileList) {
        this.fileName = response.orgdata
        this.$message({
          message: '上传成功',
          type: 'success',
          duration: 1500
        })
      },
      downloadVideo () {
        console.log(this.downFileName)
        let link = document.createElement('a')
        link.href = this.$http.adornUrl('/video/downFile?fileName=' + this.downFileName)
        link.download = '下载合成结果文件'
        document.body.appendChild(link)
        link.click()
        window.setTimeout(function () {
          document.body.removeChild(link)
        }, 0)
        this.$message({
          message: '下载成功',
          type: 'success',
          duration: 1500
        })
      },
      handleVideo () {
        this.handelLoading = true
        this.$http({
          url: this.$http.adornUrl('/video/deal'),
          method: 'post',
          data: this.$http.adornData({
            'chnlType': this.dataForm.chnlType,
            'speed': this.dataForm.speed,
            'pitch': this.dataForm.pitch,
            'volume': this.dataForm.volume,
            'voiceType': this.dataForm.voiceType,
            'fileName': this.fileName
          })
        }).then(({data}) => {
          console.log(data)
          if (data && data.code === 0) {
            this.downFileName = data.orgdata
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500
            })
          } else {
            this.$message.error(data.msg)
          }
        })
        this.handelLoading = false
      }
    }
  }
</script>
<style>
  .video-div-expand {
    margin-top: 50px;
    margin-left: 50px;
    margin-right: 0;
    font-size: 0;
  }
</style>
