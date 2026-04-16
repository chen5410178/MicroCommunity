<template>
  <div class="org-manage">
    <el-row :gutter="20">
      <el-col :span="6">
        <org-tree @switchOrg="handleSwitchOrg" />
      </el-col>
      <el-col :span="18">
        <div class="search-wrapper">
          <div class="search-title">{{ $t('common.queryCondition') }}</div>
          <div class="search-content">
            <el-input v-model="staffName" :placeholder="$t('org.staffNamePlaceholder')" class="search-item" clearable
              @keyup.enter.native="queryOrgMethod" />
            <el-button type="primary" @click="queryOrgMethod">{{ $t('common.search') }}</el-button>
          </div>
        </div>


        <div class="list-wrapper">
          <div class="list-header">
            <div class="list-title">{{ $t('org.relatedStaff') }}</div>
            <el-button type="primary" @click="openOrgRelStaff"><i class="el-icon-plus"></i>
              {{ $t('org.relatedStaff') }}</el-button>
          </div>

          <el-table :data="staffs" border>
            <el-table-column prop="name" :label="$t('org.name')" align="center" />
            <el-table-column prop="tel" :label="$t('org.phone')" align="center" />
            <el-table-column prop="email" :label="$t('org.email')" align="center" />
            <el-table-column prop="address" :label="$t('org.address')" align="center" />
            <el-table-column prop="sex" :label="$t('org.gender')" align="center">
              <template #default="{ row }">
                {{ row.sex == 0 ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('org.operation')" align="center">
              <template #default="{ row }">
                <el-button v-if="hasPrivilege('502022082955220005') || hasPrivilege('502022092283880005')" type="text"
                  @click="openDeleteOrgRelStaff(row)">
                  {{ $t('common.delete') }}
                </el-button>
                <el-button type="text" @click="toStaffDetail(row)">
                  {{ $t('common.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination :current-page="currentPage" :page-size="pageSize" :total="total"
            @current-change="handlePageChange" />
        </div>

      </el-col>
    </el-row>

    <org-rel-staff ref="orgRelStaff" @refresh="listStaffs" />
    <delete-org-rel-staff ref="deleteOrgRelStaff" @refresh="listStaffs" />
  </div>
</template>

<script>
import { listStaffs } from '@/api/org/orgApi';
import OrgTree from '@/components/org/OrgTree.vue';
import OrgRelStaff from '@/components/org/OrgRelStaff.vue';
import DeleteOrgRelStaff from '@/components/org/DeleteOrgRelStaff.vue';

export default {
  name: 'OrgList',
  components: {
    OrgTree,
    OrgRelStaff,
    DeleteOrgRelStaff,
  },
  data() {
    return {
      staffName: '',
      orgName: '',
      orgId: '',
      staffs: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
    };
  },
  methods: {
    handleSwitchOrg({ orgId, orgName }) {
      console.log(orgId, orgName);
      this.orgId = orgId;
      this.orgName = orgName;
      this.listStaffs();
    },
    async listStaffs() {
      try {
        const params = {
          page: this.currentPage,
          row: this.pageSize,
          orgId: this.orgId,
          staffName: this.staffName.trim(),
        };
        const {staffs,total} = await listStaffs( params );
        this.staffs = staffs;
        this.total = total;
      } catch (error) {
        console.error('Failed to fetch staffs:', error);
      }
    },
    queryOrgMethod() {
      this.currentPage = 1;
      this.listStaffs();
    },
    resetOrgMethod() {
      this.staffName = '';
      this.currentPage = 1;
      this.listStaffs();
    },
    openOrgRelStaff() {
      if (!this.orgId) {
        this.$message.warning('请选择组织');
        return;
      }
      this.$refs.orgRelStaff.show(this.orgId);
    },
    openDeleteOrgRelStaff(staff) {
      this.$refs.deleteOrgRelStaff.show(staff);
    },
    toStaffDetail(staff) {
      this.$router.push(`/views/staff/staffDetail?staffId=${staff.userId}`)

    },
    handlePageChange(page) {
      this.currentPage = page;
      this.listStaffs();
    },
  },
};
</script>

<style scoped>
.search-wrapper {
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;

  .search-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 20px;
    text-align: left;
    color: #333;
  }

  .search-content {
    display: flex;
    align-items: center;
    gap: 20px;

    .search-item {
      width: 200px;
    }
  }
}

.list-wrapper {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;

  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .list-title {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}

.org-manage {
  padding: 20px;
}

.margin-top-xs {
  margin-top: 10px;
}
</style>