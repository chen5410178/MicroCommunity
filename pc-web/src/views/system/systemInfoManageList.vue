<template>
  <div class="system-info-manage-container">
    <el-row>
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('systemInfo.title') }}</span>
            <el-button type="primary" size="small" class="float-right" @click="openEditDialog">
              {{ $t('common.edit') }}
            </el-button>
          </div>

          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.systemTitle') }}：</label>
                <span>{{ systemInfo.systemTitle }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.subSystemTitle') }}：</label>
                <span>{{ systemInfo.subSystemTitle }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.systemSimpleTitle') }}：</label>
                <span>{{ systemInfo.systemSimpleTitle }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.companyName') }}：</label>
                <span>{{ systemInfo.companyName }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.logoUrl') }}：</label>
                <span>{{ systemInfo.logoUrl }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.imgUrl') }}：</label>
                <span>{{ systemInfo.imgUrl }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.defaultCommunityId') }}：</label>
                <span>{{ systemInfo.defaultCommunityId }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.ownerTitle') }}：</label>
                <span>{{ systemInfo.ownerTitle }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.propertyTitle') }}：</label>
                <span>{{ systemInfo.propertyTitle }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.qqMapKey') }}：</label>
                <span>{{ systemInfo.qqMapKey }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('systemInfo.mallUrl') }}：</label>
                <span>{{ systemInfo.mallUrl }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <edit-system-info ref="editSystemInfoRef" :form-data="systemInfo" @success="handleEditSuccess" />
  </div>
</template>

<script>
import { listSystemInfo } from '@/api/system/systemInfoManageApi'
import EditSystemInfo from '@/components/system/EditSystemInfo'

export default {
  name: 'SystemInfoManageList',
  components: {
    EditSystemInfo
  },
  data() {
    return {
      systemInfo: {
        systemId: '',
        systemTitle: '',
        subSystemTitle: '',
        companyName: '',
        logoUrl: '',
        imgUrl: '',
        defaultCommunityId: '',
        ownerTitle: '',
        propertyTitle: '',
        qqMapKey: '',
        mallUrl: '',
        systemSimpleTitle: ''
      },
      editDialogVisible: false
    }
  },
  created() {
    this.getSystemInfo()
  },
  methods: {
    async getSystemInfo() {
      try {
        const params = {
          page: 1,
          row: 1
        }
        const { data } = await listSystemInfo(params)
        if (data && data.length > 0) {
          this.systemInfo = data[0]
        }
      } catch (error) {
        this.$message.error(this.$t('systemInfo.fetchError'))
      }
    },
    openEditDialog() {
      this.$refs.editSystemInfoRef.open()
    },
    handleEditSuccess() {
      this.getSystemInfo()
      this.editDialogVisible = false
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>

<style lang="scss" scoped>
.system-info-manage-container {
  padding: 20px;

  .info-item {
    margin-bottom: 20px;
    text-align: left;

    label {
      margin-right: 10px;
    }
  }

  .float-right {
    float: right;
  }
}
</style>