<template>
  <div class="floor-share-container">
    <el-row :gutter="20">
      <el-col :span="4">
        <select-community-floor ref="selectCommunityFloor" @floor-change="handleFloorChange" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('floorShare.title') }}</span>
            <el-button v-if="floorShareInfo.conditions.floorId" type="primary" size="small" style="float: right;"
              @click="openAddFloorShareModal">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <el-table :data="floorShareInfo.floorShares" border style="width: 100%" v-loading="loading">
            <el-table-column prop="fsmId" :label="$t('floorShare.id')" align="center" />
            <el-table-column prop="floorNum" :label="$t('floorShare.floor')" align="center" />
            <el-table-column prop="meterTypeName" :label="$t('floorShare.meterType')" align="center" />
            <el-table-column prop="meterNum" :label="$t('floorShare.meterNum')" align="center" />
            <el-table-column prop="shareTypeName" :label="$t('floorShare.shareType')" align="center" />
            <el-table-column prop="formulaValue" :label="$t('floorShare.formula')" align="center">
              <template slot-scope="scope">
                {{ scope.row.formulaValue || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="configName" :label="$t('floorShare.feeItem')" align="center" />
            <el-table-column prop="sharePrice" :label="$t('floorShare.feePrice')" align="center" />
            <el-table-column prop="curDegree" :label="$t('floorShare.degree')" align="center" />
            <el-table-column prop="curReadingTime" :label="$t('floorShare.readingTime')" align="center" />
            <el-table-column prop="createTime" :label="$t('floorShare.createTime')" align="center" />
            <el-table-column :label="$t('common.operation')" align="center" width="180">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="openEditFloorShareModal(scope.row)">
                  {{ $t('common.edit') }}
                </el-button>
                <el-button size="mini" type="danger" @click="openDeleteFloorShareModal(scope.row)">
                  {{ $t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
            :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
            @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <add-floor-share ref="addFloorShare" @success="handleSuccess" />
    <edit-floor-share ref="editFloorShare" @success="handleSuccess" />
    <delete-floor-share ref="deleteFloorShare" @success="handleSuccess" />
  </div>
</template>

<script>
import { listFloorShareMeter } from '@/api/fee/floorShareApi'
import SelectCommunityFloor from '@/components/report/selectCommunityFloor'
import AddFloorShare from '@/components/fee/addFloorShare'
import EditFloorShare from '@/components/fee/editFloorShare'
import DeleteFloorShare from '@/components/fee/deleteFloorShare'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'FloorShareList',
  components: {
    SelectCommunityFloor,
    AddFloorShare,
    EditFloorShare,
    DeleteFloorShare
  },
  data() {
    return {
      loading: false,
      floorShareInfo: {
        floorShares: [],
        conditions: {
          floorId: '',
          communityId: ''
        }
      },
      page: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.floorShareInfo.conditions.communityId = getCommunityId()
    setTimeout(() => {
      this.$refs.selectCommunityFloor.open({
        callBack: this.handleFloorChange
      })
    }, 1000)
  },
  methods: {
    async listFloorShares() {
      try {
        this.loading = true
        const params = {
          page: this.page.current,
          row: this.page.size,
          floorId: this.floorShareInfo.conditions.floorId,
          communityId: this.floorShareInfo.conditions.communityId
        }
        const { data, total } = await listFloorShareMeter(params)
        this.floorShareInfo.floorShares = data
        this.page.total = total
      } catch (error) {
        this.$message.error(this.$t('floorShare.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleFloorChange(floor) {
      this.floorShareInfo.conditions.floorId = floor.floorId
      this.floorShareInfo.conditions.floorNum = floor.floorNum
      this.page.current = 1
      this.listFloorShares()
    },
    openAddFloorShareModal() {
      this.$refs.addFloorShare.open({
        floorId: this.floorShareInfo.conditions.floorId,
        floorNum: this.floorShareInfo.conditions.floorNum
      })
    },
    openEditFloorShareModal(row) {
      this.$refs.editFloorShare.open(row)
    },
    openDeleteFloorShareModal(row) {
      this.$refs.deleteFloorShare.open(row)
    },
    handleSuccess() {
      this.listFloorShares()
    },
    handleSizeChange(val) {
      this.page.size = val
      this.listFloorShares()
    },
    handleCurrentChange(val) {
      this.page.current = val
      this.listFloorShares()
    }
  }
}
</script>

<style lang="scss" scoped>
.floor-share-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
}
</style>