<template>
  <el-dialog 
    :title="$t('communitySpaceManage.openTimeTitle')" 
    :visible.sync="visible"
    width="70%"
  >
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item, index) in openTimes" :key="index">
        <div class="time-item">
          <span>{{ item.hours }}{{ $t('common.hour') }}</span>
          <el-select v-model="item.isOpen" @change="updateOpenTime(item)" style="width:100%">
            <el-option 
              :label="$t('communitySpaceManage.status1001')" 
              value="Y" 
            />
            <el-option 
              :label="$t('communitySpaceManage.status2002')" 
              value="N" 
            />
          </el-select>
        </div>
      </el-col>
    </el-row>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.close') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateCommunitySpaceOpenTime } from '@/api/community/communitySpaceManageApi'

export default {
  name: 'EditCommunitySpaceOpenTime',
  data() {
    return {
      visible: false,
      openTimes: []
    }
  },
  methods: {
    open(space) {
      this.openTimes = space.openTimes || []
      this.visible = true
    },
    
    async updateOpenTime(item) {
      try {
        await updateCommunitySpaceOpenTime(item)
        this.$message.success(this.$t('common.operationSuccess'))
      } catch (error) {
        console.error('更新开放时间失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.time-item {
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 4px;
}
.time-item span {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}
</style>