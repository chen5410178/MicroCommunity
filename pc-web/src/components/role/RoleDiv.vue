<template>
    <div class="role-div">
      <div class="role-div-header">
        <el-button type="link" size="small" @click="openAddModal">
          {{ $t('role.add') }}
        </el-button>
        <el-button type="link" size="small" @click="openEditModal">
          {{ $t('role.edit') }}
        </el-button>
        <el-button type="link" size="small" @click="openDeleteModal">
          {{ $t('role.delete') }}
        </el-button>
      </div>
      
      <div class="role-list">
        <ul>
          <li
            v-for="(role, index) in roles"
            :key="index"
            :class="{ active: currentRole.pgId === role.pgId }"
            @click="switchRole(role)"
          >
            {{ role.name }}
          </li>
        </ul>
      </div>
      
      <add-privilege-group
        ref="addPrivilegeGroup"
        @success="loadRoles"
      />
      <edit-privilege-group
        ref="editPrivilegeGroup"
        @success="loadRoles"
      />
      <delete-privilege-group
        ref="deletePrivilegeGroup"
        @success="loadRoles"
      />
    </div>
  </template>
  
  <script>
  import { queryPrivilegeGroup } from '@/api/role/roleApi'
  import AddPrivilegeGroup from './AddPrivilegeGroup'
  import EditPrivilegeGroup from './EditPrivilegeGroup'
  import DeletePrivilegeGroup from './DeletePrivilegeGroup'
  
  export default {
    name: 'RoleDiv',
    components: {
      AddPrivilegeGroup,
      EditPrivilegeGroup,
      DeletePrivilegeGroup
    },
    data() {
      return {
        roles: [],
        currentRole: {}
      }
    },
    created() {
      this.loadRoles()
    },
    methods: {
      async loadRoles() {
        try {
          const params = {
            communityId: '-1'
          }
          const res = await queryPrivilegeGroup(params)
          this.roles = res
          if (this.roles.length > 0) {
            this.switchRole(this.roles[0])
          }
        } catch (error) {
          this.$message.error(error.message)
        }
      },
      switchRole(role) {
        this.currentRole = role
        this.$emit('switch-role', role)
      },
      openAddModal() {
        this.$refs.addPrivilegeGroup.open()
      },
      openEditModal() {
        if (!this.currentRole.pgId) {
          this.$message.warning(this.$t('role.selectRole'))
          return
        }
        this.$refs.editPrivilegeGroup.show(this.currentRole)
      },
      openDeleteModal() {
        if (!this.currentRole.pgId) {
          this.$message.warning(this.$t('role.selectRole'))
          return
        }
        this.$refs.deletePrivilegeGroup.show(this.currentRole)
      }
    }
  }
  </script>
  
  <style lang="scss" scoped>
  .role-div {
    background: #fff;
    border-radius: 4px;
    padding: 15px;
    margin-top: 10px;
    
    .role-div-header {
      margin-bottom: 15px;
      .el-button {
        margin-right: 10px;
      }
    }
    
    .role-list {
      ul {
        list-style: none;
        padding: 0;
        margin: 0;
        
        li {
          padding: 8px 10px;
          cursor: pointer;

          text-align: left;
          
          &:hover {
            background: #f5f7fa;
          }
          
          &.active {
            background: #EAF0FE;
            color: #676a6c;
          }
        }
      }
    }
  }
  </style>