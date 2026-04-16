<template>
  <div class="community-setting-manage-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="setting-type-card">
          <div class="setting-type-list">
            <div v-for="(item, index) in settingTypes" :key="index" class="setting-type-item"
              :class="{ 'active': conditions.settingType === item.statusCd }" @click="handleSwitchSettingType(item)">
              {{ item.name }}
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header" class="flex justify-between">
            <span>{{ currentSettingName }}</span>
          </div>
          <div class="setting-content text-left">
            <el-form label-position="right" label-width="120px">
              <el-form-item v-for="(item, index) in settingKeys" :key="index" :label="item.settingName">
                <el-input v-model="item.settingValue"
                  :placeholder="$t('communitySettingManage.inputPlaceholder', { name: item.settingName })" />
                <div class="setting-remark">{{ item.remark }}</div>
              </el-form-item>
            </el-form>
            <div class="setting-tips">{{ $t('communitySettingManage.settingTips') }}</div>
            <div class="text-right">
              <el-button type="primary" @click="handleSaveSettings">
                <i class="el-icon-check"></i>
                {{ $t('common.submit') }}
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getDict } from '@/api/community/communityApi'
import {
  listCommunitySettingKey,
  saveCommunitySetting
} from '@/api/system/communitySettingManageApi'

export default {
  name: 'CommunitySettingManageList',
  data() {
    return {
      communityId: '',
      settingTypes: [],
      settingKeys: [],
      currentSettingName: '',
      conditions: {
        settingType: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.conditions.communityId = this.communityId
    this.initData()
  },
  methods: {
    async initData() {
      try {
        const data = await getDict('community_setting_key', 'setting_type')
        this.settingTypes = data
        if (data && data.length > 0) {
          this.handleSwitchSettingType(data[0])
        }
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async handleSwitchSettingType(item) {
      this.conditions.settingType = item.statusCd
      this.currentSettingName = item.name
      await this.loadSettingKeys()
    },
    async loadSettingKeys() {
      try {
        const params = {
          communityId: this.communityId,
          settingType: this.conditions.settingType
        }
        const { data } = await listCommunitySettingKey(params)
        this.settingKeys = data.map(item => {
          return {
            ...item,
            settingValue: item.settingValue || ''
          }
        })
      } catch (error) {
        console.error('获取设置项失败:', error)
      }
    },
    async handleSaveSettings() {
      try {
        const params = {
          communityId: this.communityId,
          keys: this.settingKeys,
          settingType: this.conditions.settingType
        }
        await saveCommunitySetting(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.loadSettingKeys()
      } catch (error) {
        console.error('保存设置失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.community-setting-manage-container {
  padding: 20px;

  .setting-type-card {
    height: 100%;

    .setting-type-list {
      .setting-type-item {
        padding: 10px;
        margin-bottom: 5px;
        cursor: pointer;
        border-radius: 4px;
        text-align: center;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #409eff;
          color: white;
        }
      }
    }
  }

  .setting-content {
    padding: 20px;

    .setting-remark {
      margin-top: 5px;
      font-size: 12px;
      color: #909399;
    }

    .setting-tips {
      margin: 20px 0;
      font-size: 12px;
      color: #f56c6c;
    }
  }
}
</style>