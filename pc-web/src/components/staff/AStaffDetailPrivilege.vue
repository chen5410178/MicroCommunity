<template>
    <div class="staff-privilege-container">
      <el-row :gutter="20">
        <el-col :span="18">
          <el-card class="box-card">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('staffDetailPrivilege.orgs') }}</span>
            </div>
            <el-row v-for="(item, index) in aStaffDetailPrivilegeInfo.orgs" :key="index" :gutter="20">
              <el-col :span="12">{{ $t('staffDetailPrivilege.orgName') }}: {{ item.orgName }}</el-col>
              <el-col :span="12">{{ $t('staffDetailPrivilege.relCdName') }}: {{ item.relCdName }}</el-col>
            </el-row>
          </el-card>
          <el-card class="box-card" style="margin-top: 20px;">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('staffDetailPrivilege.roles') }}</span>
            </div>
            <div v-for="(item, index) in aStaffDetailPrivilegeInfo.roles" :key="index">
              
                {{ item.roleName }} 
                <p v-if="item.roleCommunityDtoList && item.roleCommunityDtoList.length > 0">
                  (
                <span v-for="(item1, index1) in item.roleCommunityDtoList" :key="index1">
                  {{ item1.communityName }}
                  {{ index1 === item.roleCommunityDtoList.length - 1 ? '' : '、' }}
                </span> )
              </p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('staffDetailPrivilege.privileges') }}</span>
            </div>
            <el-tree
              :data="treeData"
              :props="{ label: 'text', children: 'children' }"
              default-expand-all
              node-key="id"
            />
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { getStaffPrivileges, getStaffOrgs, getStaffRoles } from '@/api/staff/adminStaffDetailApi'
  
  export default {
    name: 'AStaffDetailPrivilege',
    data() {
      return {
        aStaffDetailPrivilegeInfo: {
          privileges: [],
          orgs: [],
          roles: [],
          staffId: ''
        },
        treeData: []
      }
    },
    methods: {
      switchTab(data) {
        this.aStaffDetailPrivilegeInfo.staffId = data.staffId
        this.loadStaffPrivileges()
        this.loadStaffOrgs()
        this.loadStaffRoles()
      },
      async loadStaffPrivileges() {
        try {
          const { data } = await getStaffPrivileges({ staffId: this.aStaffDetailPrivilegeInfo.staffId })
          this.aStaffDetailPrivilegeInfo.privileges = data.datas
          this.treeData = this.buildTreeData(data.datas)
        } catch (error) {
          this.$message.error(this.$t('staffDetailPrivilege.fetchPrivilegesError'))
        }
      },
      async loadStaffOrgs() {
        try {
          const { data } = await getStaffOrgs({ staffId: this.aStaffDetailPrivilegeInfo.staffId, page: 1, row: 1 })
          this.aStaffDetailPrivilegeInfo.orgs = data.data
        } catch (error) {
          this.$message.error(this.$t('staffDetailPrivilege.fetchOrgsError'))
        }
      },
      async loadStaffRoles() {
        try {
          const { data } = await getStaffRoles({ staffId: this.aStaffDetailPrivilegeInfo.staffId, page: 1, row: 9999 })
          this.aStaffDetailPrivilegeInfo.roles = data.data
        } catch (error) {
          this.$message.error(this.$t('staffDetailPrivilege.fetchRolesError'))
        }
      },
      buildTreeData(privileges) {
        const groupMap = new Map()
        privileges.forEach(pItem => {
          if (!groupMap.has(pItem.gId)) {
            groupMap.set(pItem.gId, {
              id: `g_${pItem.gId}`,
              gId: pItem.gId,
              text: pItem.gName,
              children: []
            })
          }
        })
  
        const menuMap = new Map()
        privileges.forEach(pItem => {
          const group = groupMap.get(pItem.gId)
          if (!menuMap.has(pItem.mId)) {
            menuMap.set(pItem.mId, {
              id: `m_${pItem.mId}`,
              mId: pItem.mId,
              text: pItem.mName,
              children: []
            })
            group.children.push(menuMap.get(pItem.mId))
          }
        })
  
        privileges.forEach(pItem => {
          const menu = menuMap.get(pItem.mId)
          menu.children.push({
            id: `p_${pItem.pId}`,
            pId: pItem.pId,
            text: pItem.pName
          })
        })
  
        return Array.from(groupMap.values())
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .staff-privilege-container {
    padding: 20px;
  }
  </style>