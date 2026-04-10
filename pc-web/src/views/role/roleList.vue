<template>
  <div class="role-container padding">
    <el-row>
      <el-col :span="4" class="role-left">
        <role-div @switch-role="handleSwitchRole" />
      </el-col>
      <el-col :span="20" class="role-right">
        <div class="role-content">
          <div class="role-title">{{ currentRole.name }}</div>
          <el-divider />
          <div class="role-menu">
            <el-button :type="activeTab === 'privilege' ? 'primary' : ''" @click="changeTab('privilege')">
              {{ $t('role.privilege') }}
            </el-button>
            <!-- <el-button :type="activeTab === 'community' ? 'primary' : ''" @click="changeTab('community')">
              {{ $t('role.community') }}
            </el-button> -->
            <el-button :type="activeTab === 'staff' ? 'primary' : ''" @click="changeTab('staff')">
              {{ $t('role.staff') }}
            </el-button>
          </div>

          <div v-if="activeTab === 'privilege'">
            <privilege-tree :pg-id="currentRole.pgId" />
          </div>

          <div v-if="activeTab === 'community'">
            <role-community :pg-id="currentRole.pgId" />
          </div>

          <div v-if="activeTab === 'staff'">
            <role-staff :pg-id="currentRole.pgId" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import RoleDiv from '@/components/role/RoleDiv'
import PrivilegeTree from '@/components/role/PrivilegeTree'
// import RoleCommunity from '@/components/role/RoleCommunity'
import RoleStaff from '@/components/role/RoleStaff'

export default {
  name: 'RoleList',
  components: {
    RoleDiv,
    PrivilegeTree,
    // RoleCommunity,
    RoleStaff
  },
  data() {
    return {
      activeTab: 'privilege',
      currentRole: {
        pgId: ''
      }
    }
  },
  methods: {
    handleSwitchRole(role) {
      this.currentRole = role
      this.changeTab('privilege')
    },
    changeTab(tabName) {
      this.activeTab = tabName
    }
  }
}
</script>

<style lang="scss" scoped>
.role-container {


  .role-left {
    padding: 0px;
    margin: 0px;
  }

  .role-right {
    padding: 0px;
    margin-top: 10px;

    .el-divider--horizontal {
      display: block;
      height: 1px;
      width: 100%;
      margin: 0px 0;
    }
  }

  .role-content {
    margin-top: 0px;
    margin-left: 20px;
    background: #fff;
    border-radius: 4px;


    .role-title {
      font-size: 18px;
      line-height: 48px;
      text-align: left;
      margin-left: 15px;
    }

    .role-context {
      color: #666;
    }

    .role-menu {
      text-align: left;
      margin-left: 15px;

      .el-button {
        margin-top: 15px;

      }
    }
  }
}
</style>