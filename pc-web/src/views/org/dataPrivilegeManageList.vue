<template>
  <div class="data-privilege-container padding">
    <el-row class="data-privilege-wrapper">
      <el-col :span="4" class="left-panel">
        <data-privilege-div ref="dataPrivilegeDiv" @switch-data-privilege="handleSwitchDataPrivilege" />
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-card class="box-card">
          <div slot="header" class="text-left">
            <span>{{ $t('dataPrivilege.title') }}: {{ currentDataPrivilege.name }}</span>
            <div>{{ currentDataPrivilege.description }}</div>
          </div>
          <div class="tab-wrapper">
            <el-tabs v-model="activeTab" @tab-click="handleTabClick(activeTab)">
              <el-tab-pane :label="$t('dataPrivilege.unitAuth')" name="dataPrivilegeUnit">
                <data-privilege-unit v-if="activeTab === 'dataPrivilegeUnit'" ref="dataPrivilegeUnit"
                  />
              </el-tab-pane>
              <el-tab-pane :label="$t('dataPrivilege.staffRelation')" name="dataPrivilegeStaff">
                <data-privilege-staff v-if="activeTab === 'dataPrivilegeStaff'" ref="dataPrivilegeStaff"
                  />
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import DataPrivilegeDiv from '@/components/org/dataPrivilegeDiv'
import DataPrivilegeUnit from '@/components/org/dataPrivilegeUnit'
import DataPrivilegeStaff from '@/components/org/dataPrivilegeStaff'

import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DataPrivilegeManageList',
  components: {
    DataPrivilegeDiv,
    DataPrivilegeUnit,
    DataPrivilegeStaff,
  },
  data() {
    return {
      activeTab: 'dataPrivilegeUnit',
      currentDataPrivilege: {
        dpId: '',
        name: '',
        description: ''
      },
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    handleSwitchDataPrivilege(data) {
      this.currentDataPrivilege = { ...data }
      this.handleTabClick(this.activeTab)
    },
    handleTabClick(tab) {
      this.activeTab = tab
      setTimeout(() => {
        this.$refs[tab].open(this.currentDataPrivilege.dpId)
      }, 100)
    },
    handleRefresh() {
      this.$refs.dataPrivilegeDiv.loadData()
      if (this.activeTab === 'unit') {
        this.$refs.dataPrivilegeUnit && this.$refs.dataPrivilegeUnit.loadData()
      } else {
        this.$refs.dataPrivilegeStaff && this.$refs.dataPrivilegeStaff.loadData()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.data-privilege-container {

  height: 100%;

  .data-privilege-wrapper {
    height: 100%;

    .left-panel {
      padding-right: 0;
      height: 100%;
    }

    .right-panel {
      padding-left: 10px;
      height: 100%;

      .box-card {
        height: 100%;

        .clearfix {
          h5 {
            margin: 0 0 10px 0;
            font-size: 16px;
          }

          p {
            margin: 0;
            color: #999;
          }
        }

        .line-x {
          height: 1px;
          background-color: #e6e6e6;
          margin: 15px 0;
        }

        .tab-wrapper {
          height: calc(100% - 80px);
          overflow: auto;
        }
      }
    }
  }
}
</style>