<template>
  <div class="room-owe-fee-callable-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('roomOweFeeCallable.title') }}</span>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form label-position="right" label-width="120px" class="text-left">
            <el-form-item :label="$t('roomOweFeeCallable.callableWay')">
              <el-select 
                v-model="roomOweFeeCallableInfo.callableWay" 
                style="width:100%"
                :placeholder="$t('roomOweFeeCallable.selectCallableWay')">
                <el-option 
                  disabled 
                  value=""
                  :label="$t('roomOweFeeCallable.selectCallableWay')"/>
                <el-option 
                  label="微信模板消息" 
                  value="WECHAT"/>
                <el-option 
                  label="短信" 
                  value="SMS"/>
              </el-select>
            </el-form-item>
            
            <el-form-item :label="$t('roomOweFeeCallable.callableFees')">
              <el-checkbox-group v-model="roomOweFeeCallableInfo.feeIds">
                <el-checkbox 
                  v-for="(item,index) in roomOweFeeCallableInfo.fees" 
                  :key="index" 
                  :label="item.feeId">
                  {{item.feeName}}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            
            <el-form-item :label="$t('roomOweFeeCallable.remark')">
              <el-input
                type="textarea"
                :rows="5"
                v-model="roomOweFeeCallableInfo.remark"
                :placeholder="$t('roomOweFeeCallable.inputRemark')">
              </el-input>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      
      <div class="footer-buttons">
        <el-button 
          type="primary" 
          @click="_saveOweFeeCallable">
          <i class="el-icon-check"></i>
          {{ $t('common.submit') }}
        </el-button>
        <el-button 
          type="warning" 
          @click="$router.go(-1)">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { saveOweFeeCallable, listFee } from '@/api/fee/roomOweFeeCallableApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'RoomOweFeeCallableList',
  data() {
    return {
      roomOweFeeCallableInfo: {
        callableWay: '',
        remark: '',
        communityId: '',
        feeIds: [],
        roomId: '',
        roomIds: [],
        fees: []
      }
    }
  },
  created() {
    this.roomOweFeeCallableInfo.communityId = getCommunityId()
    this.roomOweFeeCallableInfo.roomId = this.$route.query.roomId
    this.roomOweFeeCallableInfo.roomIds.push(this.$route.query.roomId)
    this._loadRoomOweFees()
  },
  methods: {
    roomOweFeeCallableValidate() {
      if (!this.roomOweFeeCallableInfo.callableWay) {
        this.$message.error(this.$t('roomOweFeeCallable.callableWayRequired'))
        return false
      }
      return true
    },
    async _saveOweFeeCallable() {
      if (!this.roomOweFeeCallableValidate()) {
        return
      }
      
      try {
        const res = await saveOweFeeCallable(this.roomOweFeeCallableInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.submitError'))
      }
    },
    async _loadRoomOweFees() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.roomOweFeeCallableInfo.communityId,
          payerObjId: this.roomOweFeeCallableInfo.roomId,
          state: '2008001'
        }
        const { fees } = await listFee(params)
        this.roomOweFeeCallableInfo.fees = fees || []
      } catch (error) {
        this.$message.error(this.$t('roomOweFeeCallable.fetchFeeError'))
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.room-owe-fee-callable-container {
  padding: 20px;
  
  .box-card {
    margin-bottom: 20px;
  }
  
  .footer-buttons {
    text-align: right;
    margin-top: 20px;
    
    .el-button {
      margin-left: 10px;
    }
  }
  
  .el-checkbox {
    margin-right: 15px;
  }
}
</style>