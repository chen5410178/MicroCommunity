<template>
  <div class="privilege-tree">
    <el-tree
      ref="privilegeTree"
      :data="treeData"
      :props="defaultProps"
      show-checkbox
      node-key="id"
      :default-expanded-keys="expandedKeys"
      :default-checked-keys="checkedKeys"
      @check="handleCheckChange"
    ></el-tree>
  </div>
</template>

<script>
import { queryPrivilegeGroupNoAddPrivilege, addPrivilegeToPrivilegeGroup, deletePrivilegeFromPrivilegeGroup } from '@/api/role/roleApi'

export default {
  name: 'PrivilegeTree',
  props: {
    pgId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      privileges: [],
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      expandedKeys: [],
      checkedKeys: [],
      // Add a variable to store previous state
      previousCheckedKeys: []
    }
  },
  watch: {
    pgId: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.loadPrivileges(newVal)
        }
      }
    }
  },
  methods: {
    async loadPrivileges(pgId) {
      try {
        const params = {
          pgId,
        }
        const res = await queryPrivilegeGroupNoAddPrivilege(params)
        this.privileges = res
        this.checkedKeys= []
        this.formatTreeData()
        // Store initial checked state
        this.previousCheckedKeys = [...this.checkedKeys]
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    formatTreeData() {
      const groups = []
      const expandedKeys = []
      
      this.privileges.forEach(item => {
        if (!groups.some(g => g.gId === item.gId)) {
          const group = {
            id: `g_${item.gId}`,
            gId: item.gId,
            text: item.gName,
            children: this.formatMenuData(item.gId)
          }
          groups.push(group)
          expandedKeys.push(`g_${item.gId}`)
        }
      })
      
      this.treeData = groups
      this.expandedKeys = expandedKeys
    },
    formatMenuData(gId) {
      const menus = []
      this.privileges
        .filter(item => item.gId === gId)
        .forEach(item => {
          if (!menus.some(m => m.mId === item.mId)) {
            const menu = {
              id: `m_${item.mId}`,
              mId: item.mId,
              text: item.mName,
              children: this.formatPrivilegeData(item.mId)
            }
            menus.push(menu)
          }
        })
      return menus
    },
    formatPrivilegeData(mId) {
      return this.privileges
        .filter(item => item.mId === mId)
        .map(item => {
          const node = {
            id: `p_${item.pId}`,
            pId: item.pId,
            text: item.pName
          }
          if (item.pgId) {
            this.checkedKeys.push(`p_${item.pId}`)
          }
          return node
        })
    },
    async handleCheckChange(data, { checkedKeys }) {
      // Get current checked privilege IDs
      const currentPrivilegeIds = checkedKeys
        .filter(key => key.startsWith('p_'))
        .map(key => key.replace('p_', ''))
      
      // Find added privileges
      const addedPrivileges = currentPrivilegeIds.filter(id => 
        !this.previousCheckedKeys.includes(`p_${id}`)
      )
      
      // Find removed privileges
      const removedPrivileges = this.previousCheckedKeys
        .filter(key => key.startsWith('p_'))
        .map(key => key.replace('p_', ''))
        .filter(id => !currentPrivilegeIds.includes(id))
      
      try {
        if (addedPrivileges.length > 0) {
          await addPrivilegeToPrivilegeGroup({
            pgId: this.pgId,
            pIds: addedPrivileges.map(pId => ({ pId }))
          })
        }
        if (removedPrivileges.length > 0) {
          await deletePrivilegeFromPrivilegeGroup({
            pgId: this.pgId,
            pIds: removedPrivileges.map(pId => ({ pId }))
          })
        }
        
        // Update previous state only after successful API calls
        this.previousCheckedKeys = [...checkedKeys]
      } catch (error) {
        this.$message.error(error.message)
        // Revert changes if API call fails
        this.$nextTick(() => {
          this.$refs.privilegeTree.setCheckedKeys([...this.previousCheckedKeys])
        })
      }
    }
  }
}
</script>

<style scoped>
.privilege-tree {
  background: #fff;
  border-radius: 4px;
  padding: 15px;
  margin-top: 10px;
}
</style>