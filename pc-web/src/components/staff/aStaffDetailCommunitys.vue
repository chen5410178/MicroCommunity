<template>
  <div >
    <el-row>
      <el-col :span="24" class="text-right">
        <el-button type="primary" size="small" @click="openAddStaffCommunityModal">
          {{ $t('aStaffDetailCommunitys.relateCommunity') }}
        </el-button>
      </el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="communitys" border style="width: 100%">
        <el-table-column prop="staffName" :label="$t('aStaffDetailCommunitys.staffName')" align="center" />
        <el-table-column prop="staffId" :label="$t('aStaffDetailCommunitys.staffId')" align="center" />
        <el-table-column prop="communityId" :label="$t('aStaffDetailCommunitys.communityId')" align="center" />
        <el-table-column prop="communityName" :label="$t('aStaffDetailCommunitys.communityName')" align="center" />
        <el-table-column :label="$t('aStaffDetailCommunitys.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="openDeleteRoleCommunityModel(scope.row)">
              {{ $t('aStaffDetailCommunitys.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12" class="text-right">
          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-col>
      </el-row>
    </div>
    
    <add-a-staff-community ref="addCommunity" @success="handleAddCommunitySuccess" />
    <delete-a-staff-community ref="deleteCommunity" @success="handleDeleteCommunitySuccess" />
  </div>
</template>

<script>
import { listStaffCommunities } from '@/api/staff/aStaffDetailApi'
import AddAStaffCommunity from '@/components/staff/addAStaffCommunity'
import DeleteAStaffCommunity from '@/components/staff/deleteAStaffCommunity'

export default {
  name: 'AStaffDetailCommunitys',
  props: {
    staffId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      communitys: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
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
  components:{
    AddAStaffCommunity,
    DeleteAStaffCommunity,
  },
  methods: {
    async loadData() {
      try {
        const response = await listStaffCommunities({
          staffId: this.staffId,
          page: this.pagination.current,
          row: this.pagination.size
        })
        this.communitys = response.data || []
        this.pagination.total = response.total || 0
      } catch (error) {
        this.$message.error(this.$t('aStaffDetailCommunitys.loadError'))
      }
    },
    openAddStaffCommunityModal() {
      this.$refs.addCommunity.open({
        staffId: this.staffId
      })
    },
    openDeleteRoleCommunityModel(community) {
      this.$refs.deleteCommunity.open(community)
    },
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    handleAddCommunitySuccess() {
      this.loadData()
      this.$message.success(this.$t('common.operationSuccess'))
    },
    handleDeleteCommunitySuccess() {
      this.loadData()
      this.$message.success(this.$t('common.operationSuccess'))
    }
  }
}
</script>

