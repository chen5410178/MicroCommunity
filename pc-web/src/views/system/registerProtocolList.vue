<template>
  <div class="register-protocol-container">
    <el-row>
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('registerProtocol.title') }}</span>
          </div>
          <div class="card-content">
            <el-form label-position="right" label-width="120px">
              <el-form-item :label="$t('registerProtocol.userProtocol')">
                <div class="editor-container">
                  <rich-text-editor ref="userProtocolEditor" v-model="registerProtocolInfo.userProtocol" />
                </div>
              </el-form-item>
              <el-form-item :label="$t('registerProtocol.merchantProtocol')">
                <div class="editor-container">
                  <rich-text-editor ref="merchantProtocolEditor" v-model="registerProtocolInfo.merchantProtocol" />
                </div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="updateRegisterProtocol">
                  {{ $t('common.update') }}
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listRegisterProtocols, updateRegisterProtocol } from '@/api/system/registerProtocolApi'
import RichTextEditor from '@/components/editor/RichTextEditor.vue'

export default {
  name: 'RegisterProtocolList',
  components: {
    RichTextEditor
  },
  data() {
    return {
      registerProtocolInfo: {
        userProtocol: '',
        merchantProtocol: '',
        protolcolId: ''
      }
    }
  },
  created() {
    this.listRegisterProtocols()
  },
  methods: {
    async listRegisterProtocols() {
      try {
        const params = {
          page: 1,
          row: 1
        }
        const res = await listRegisterProtocols(params)
        if (res.data && res.data.length > 0) {

          this.registerProtocolInfo = {
            ...res.data[0],
            protolcolId: res.data[0].protolcolId || ''
          }
          if (this.$refs.userProtocolEditor) {
            this.$refs.userProtocolEditor.setContent(this.registerProtocolInfo.userProtocol || '');
          }
          if (this.$refs.merchantProtocolEditor) {
            this.$refs.merchantProtocolEditor.setContent(this.registerProtocolInfo.merchantProtocol || '');
          }
        }
      } catch (error) {
        this.$message.error(this.$t('registerProtocol.fetchError'))
      }
    },
    async updateRegisterProtocol() {
      try {
        await updateRegisterProtocol(this.registerProtocolInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.listRegisterProtocols()
      } catch (error) {
        this.$message.error(error.message || this.$t('common.updateFailed'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.register-protocol-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-content {
    padding: 20px;
  }

  .editor-container {
    margin-bottom: 20px;
  }
}
</style>