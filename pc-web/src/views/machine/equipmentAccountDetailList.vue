<template>
  <div class="equipment-account-detail-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('equipmentAccountDetail.title') }}</span>
        <div class="card-header-actions">
          <el-button size="small" @click="_goBack()">
            <i class="el-icon-close"></i>
            {{ $t('equipmentAccountDetail.back') }}
          </el-button>
          <el-button type="primary" size="small" @click="_printEquipmentDetail()">
            <i class="el-icon-printer"></i>
            {{ $t('equipmentAccountDetail.print') }}
          </el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.machineName') }}：</label>
            <span>{{ equipmentAccountDetail.machineName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.machineCode') }}：</label>
            <span>{{ equipmentAccountDetail.machineCode }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.levelName') }}：</label>
            <span>{{ equipmentAccountDetail.levelName }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.stateName') }}：</label>
            <span>{{ equipmentAccountDetail.stateName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.brand') }}：</label>
            <span>{{ equipmentAccountDetail.brand }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.model') }}：</label>
            <span>{{ equipmentAccountDetail.model }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.locationDetail') }}：</label>
            <span>{{ equipmentAccountDetail.locationDetail }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.firstEnableTime') }}：</label>
            <span>{{ equipmentAccountDetail.firstEnableTime }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.warrantyDeadline') }}：</label>
            <span>{{ equipmentAccountDetail.warrantyDeadline }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.usefulLife') }}：</label>
            <span>{{ equipmentAccountDetail.usefulLife }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.purchasePrice') }}：</label>
            <span>{{ equipmentAccountDetail.purchasePrice }} {{ $t('equipmentAccountDetail.yuan') }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.netWorth') }}：</label>
            <span>{{ equipmentAccountDetail.netWorth }} {{ $t('equipmentAccountDetail.yuan') }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.useUserName') }}：</label>
            <span>{{ equipmentAccountDetail.useUserName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.chargeUseName') }}：</label>
            <span>{{ equipmentAccountDetail.chargeUseName }}</span>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="16">
          <div class="form-item">
            <label>{{ $t('equipmentAccountDetail.remark') }}：</label>
            <span>{{ equipmentAccountDetail.remark }}</span>
          </div>
        </el-col>
      </el-row>

    </el-card>
    <el-card>
      <div slot="header" class="flex justify-between">
        <span>{{ $t('equipmentAccountDetail.logTitle') }}</span>
      </div>
      <el-row :gutter="20" >
        <el-col :span="24">
          <el-table :data="equipmentAccountDetail.elogs" border style="width: 100%">
            <el-table-column type="index" :label="$t('equipmentAccountDetail.serialNumber')" width="80"
              align="center" />
            <el-table-column prop="operatingCode" :label="$t('equipmentAccountDetail.operatingCode')" align="center" />
            <el-table-column prop="operatingDescriptor" :label="$t('equipmentAccountDetail.operatingDescriptor')"
              align="center" />
            <el-table-column prop="createTime" :label="$t('equipmentAccountDetail.createTime')" align="center" />
            <el-table-column prop="remark" :label="$t('equipmentAccountDetail.remark')" align="center" />
          </el-table>
        </el-col>
      </el-row>
    </el-card>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { getEquipmentAccountDetail, getEquipmentLogs } from '@/api/machine/equipmentAccountDetailApi'
import ViewImage from '@/components/system/viewImage'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EquipmentAccountDetailList',
  components: {
    ViewImage
  },
  data() {
    return {
      equipmentAccountDetail: {
        machineId: '',
        machineName: '',
        machineCode: '',
        brand: '',
        model: '',
        locationDetail: '',
        firstEnableTime: '',
        warrantyDeadline: '',
        usefulLife: '',
        importanceLevel: '',
        state: '',
        stateName: '',
        levelName: '',
        purchasePrice: '',
        netWorth: '',
        useOrgId: '',
        useOrgName: '',
        useUserId: '',
        useUserName: '',
        useUseTel: '',
        chargeOrgId: '',
        chargeOrgName: '',
        chargeOrgTel: '',
        chargeUseId: '',
        chargeUseName: '',
        remark: '',
        elogs: []
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    const machineId = this.$route.query.machineId
    if (!machineId) {
      this.$message.error(this.$t('equipmentAccountDetail.illegalOperation'))
      this.$router.push('/machine/equipmentAccount')
      return
    }
    this.equipmentAccountDetail.machineId = machineId
    this._listEquipmentDetail()
  },
  methods: {
    async _listEquipmentDetail() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.communityId,
          machineId: this.equipmentAccountDetail.machineId
        }
        const { data } = await getEquipmentAccountDetail(params)
        if (data.length < 1) {
          this.$message.error(this.$t('equipmentAccountDetail.dataError'))
          this.$router.push('/machine/equipmentAccount')
          return
        }
        this.equipmentAccountDetail = { ...this.equipmentAccountDetail, ...data[0] }
        this._loadEquipmentLogs()
      } catch (error) {
        console.error('Failed to fetch equipment detail:', error)
      }
    },
    async _loadEquipmentLogs() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId,
          machineId: this.equipmentAccountDetail.machineId
        }
        const { data } = await getEquipmentLogs(params)
        this.equipmentAccountDetail.elogs = data
      } catch (error) {
        console.error('Failed to fetch equipment logs:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    },
    openFile(photo) {
      this.$refs.viewImage.open(photo.url)
    },
    _printEquipmentDetail() {
      window.open(`/#/views/resource/printEquipmentAccountLabel?machineId=${this.equipmentAccountDetail.machineId}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.equipment-account-detail-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .card-header-actions {
    float: right;
  }

  .form-item {
    margin-bottom: 18px;
    text-align: left;

    label {
      margin-right: 10px;
    }

    span {
      color: #606266;
    }
  }

  .mt-20 {
    margin-top: 20px;
  }
}
</style>