<template>
  <div class="add-room-repair-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('addRoomRepair.title') }}</span>
      </div>
      <el-form ref="form" :model="addRoomRepairInfo" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addRoomRepair.repairType')" prop="repairType">
              <el-select 
                v-model="addRoomRepairInfo.repairType" 
                :placeholder="$t('addRoomRepair.repairTypePlaceholder')"
                style="width:100%"
              >
                <el-option 
                  v-for="(item,index) in addRoomRepairInfo.repairSettings"
                  :key="index"
                  :label="item.repairTypeName"
                  :value="item.repairType"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addRoomRepair.repairName')" prop="repairName">
              <el-input 
                v-model="addRoomRepairInfo.repairName" 
                :placeholder="$t('addRoomRepair.repairNamePlaceholder')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addRoomRepair.tel')" prop="tel">
              <el-input 
                v-model="addRoomRepairInfo.tel" 
                type="number"
                :placeholder="$t('addRoomRepair.telPlaceholder')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addRoomRepair.appointmentTime')" prop="appointmentTime">
              <el-date-picker
                v-model="addRoomRepairInfo.appointmentTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                :placeholder="$t('addRoomRepair.appointmentTimePlaceholder')"
                style="width:100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item :label="$t('addRoomRepair.context')" prop="context">
              <el-input
                type="textarea"
                :rows="5"
                v-model="addRoomRepairInfo.context"
                :placeholder="$t('addRoomRepair.contextPlaceholder')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24" style="text-align: right;">
            <el-button type="warning" @click="_goBack">{{ $t('addRoomRepair.back') }}</el-button>
            <el-button type="primary" @click="saveOwnerRepairInfo">{{ $t('addRoomRepair.submit') }}</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { saveOwnerRepair, listRepairSettings } from '@/api/work/addRoomRepairApi'
import { getCommunityId } from '@/api/community/communityApi'
import { getDateStr } from '@/utils/dateUtil'

export default {
  name: 'AddRoomRepairList',
  data() {
    return {
      addRoomRepairInfo: {
        repairId: '',
        repairType: '',
        repairName: '',
        tel: '',
        appointmentTime: '',
        context: '',
        repairObjType: '004',
        repairObjId: '',
        repairObjName: '',
        repairSettings: [],
        communityId: ''
      }
    }
  },
  created() {
    this._initAddOwnerRepairInfo()
    this.addRoomRepairInfo.repairObjId = this.$route.query.roomId
    this.addRoomRepairInfo.repairObjName = this.$route.query.roomName
    this.addRoomRepairInfo.communityId = getCommunityId()
    this._listRepairSettings(1, 50, 'F')

  },
  methods: {
    addOwnerRepairValidate() {
      return this.$refs.form.validate()
    },
    async saveOwnerRepairInfo() {
      try {
        const valid = await this.addOwnerRepairValidate()
        if (!valid) return

        const res = await saveOwnerRepair(this.addRoomRepairInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this._goBack()
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('addRoomRepair.submitError'))
      }
    },
    _initAddOwnerRepairInfo() {
      this.addRoomRepairInfo.appointmentTime = getDateStr()
    },
    async _listRepairSettings(page, rows, publicArea) {
      try {
        const params = {
          page,
          row: rows,
          communityId: this.addRoomRepairInfo.communityId,
          publicArea
        }
        const res = await listRepairSettings(params)
        this.addRoomRepairInfo.repairSettings = res.data
      } catch (error) {
        console.error('获取报修设置失败:', error)
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-room-repair-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
  }
  
  .el-row {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .el-col {
    width: 100%;
  }
}
</style>