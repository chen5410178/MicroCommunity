<template>
  <el-row class="margin-top" :gutter="20">
    <el-col :span="18">
      <el-card>
        <div class="text-left">
          <h5>{{ $t('staffDetailOrgPrivilege.relatedOrg') }}</h5>
          <el-row v-for="(item, index) in orgs" :key="index" class="margin-top">
            <el-col :span="12">{{ $t('staffDetailOrgPrivilege.org') }}：{{ item.orgName }}</el-col>
            <el-col :span="12">{{ $t('staffDetailOrgPrivilege.position') }}：{{ item.relCdName }}</el-col>
          </el-row>
        </div>
      </el-card>

      <el-card class="margin-top">
        <div class="text-left">
          <h5>{{ $t('staffDetailOrgPrivilege.relatedRole') }}</h5>
          <el-row v-for="(item, index) in roles" :key="index">
            <el-col :span="24">
              <p v-if="item.roleCommunityDtoList.length > 0">{{ item.roleName }}</p>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-col>

    <el-col :span="6">
      <el-card>
        <div class="mail-box-header border-radius-top text-left">
          <h2>{{ $t('staffDetailOrgPrivilege.staffPrivilege') }}</h2>
        </div>

        <el-tree :data="privilegesTree" node-key="id" default-expand-all :props="treeProps" :expand-on-click-node="false">
        </el-tree>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
import { getStaffOrgs, getStaffRoles, getStaffPrivileges } from '@/api/staff/aStaffDetailApi'

export default {
  name: 'StaffDetailOrgPrivilege',
  props: {
    staffId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      orgs: [],
      roles: [],
      privileges: [],
      privilegesTree: [],
      treeProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  watch: {
    staffId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadData()
        }
      }
    }
  },
  methods: {
    async loadData() {
      await Promise.all([
        this.loadStaffOrgs(),
        this.loadStaffRoles(),
        this.loadStaffPrivileges()
      ])
      this.buildPrivilegesTree()
    },
    async loadStaffOrgs() {
      try {
        const response = await getStaffOrgs({
          staffId: this.staffId,
          page: 1,
          row: 999
        })
        this.orgs = response.data || []
      } catch (error) {
        this.$message.error(this.$t('staffDetailOrgPrivilege.loadOrgsError'))
      }
    },
    async loadStaffRoles() {
      try {
        const response = await getStaffRoles({
          staffId: this.staffId,
          page: 1,
          row: 999
        })
        this.roles = response.data || []
      } catch (error) {
        this.$message.error(this.$t('staffDetailOrgPrivilege.loadRolesError'))
      }
    },
    async loadStaffPrivileges() {
      try {
        const response = await getStaffPrivileges({
          staffId: this.staffId
        })
        this.privileges = response.datas || []
      } catch (error) {
        this.$message.error(this.$t('staffDetailOrgPrivilege.loadPrivilegesError'))
      }
    },
    buildPrivilegesTree() {
      const groupMap = new Map()

      // 构建组节点
      this.privileges.forEach(item => {
        if (!groupMap.has(item.gId)) {
          groupMap.set(item.gId, {
            id: `g_${item.gId}`,
            gId: item.gId,
            text: item.gName,
            children: []
          })
        }
      })

      // 构建菜单节点
      this.privileges.forEach(item => {
        const group = groupMap.get(item.gId)
        let menu = group.children.find(m => m.mId === item.mId)

        if (!menu) {
          menu = {
            id: `m_${item.mId}`,
            mId: item.mId,
            text: item.mName,
            children: []
          }
          group.children.push(menu)
        }

        // 添加权限节点
        menu.children.push({
          id: `p_${item.pId}`,
          pId: item.pId,
          text: item.pName
        })
      })

      this.privilegesTree = Array.from(groupMap.values())
    }
  }
}
</script>

