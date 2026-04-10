<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>

    <div class="margin-top">
      <el-table :data="aCarDetailOwnerInfo.owners" style="width: 100%">
        <el-table-column :label="$t('carDetailOwner.ownerFace')" align="center" width="100">
          <template slot-scope="scope">
            <el-image style="width: 60px; height: 60px;" :src="scope.row.url || '/img/noPhoto.jpg'" fit="cover"
              @click="_viewOwnerFace(scope.row.url)" class="border-radius">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailOwner.name')" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailOwner.gender')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('carDetailOwner.male') : $t('carDetailOwner.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('carDetailOwner.idCard')" align="center"></el-table-column>
        <el-table-column prop="address" :label="$t('carDetailOwner.address')" align="center"></el-table-column>
        <el-table-column prop="roomCount" :label="$t('carDetailOwner.roomCount')" align="center"></el-table-column>
        <el-table-column prop="memberCount" :label="$t('carDetailOwner.memberCount')" align="center"></el-table-column>
        <el-table-column prop="carCount" :label="$t('carDetailOwner.carCount')" align="center"></el-table-column>
        <el-table-column prop="complaintCount" :label="$t('carDetailOwner.complaintCount')"
          align="center"></el-table-column>
        <el-table-column prop="repairCount" :label="$t('carDetailOwner.repairCount')" align="center"></el-table-column>
        <el-table-column prop="oweFee" :label="$t('carDetailOwner.oweFee')" align="center"></el-table-column>
        <el-table-column prop="contractCount" :label="$t('carDetailOwner.contractCount')"
          align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="_toACarDetailOwnerDetail(scope.row)">
              {{ $t('carDetailOwner.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-row class="margin-top">
        <el-col :span="12"></el-col>
        <el-col :span="12" class="text-right">
          <el-pagination @current-change="handlePageChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>

    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import { queryAdminOwners } from '@/api/aCommunity/aCarDetailOwnerApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'ACarDetailOwner',
  components: {
    ViewImage
  },
  data() {
    return {
      aCarDetailOwnerInfo: {
        owners: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.handleSwitch(data)
    },
    handleSwitch(data) {
      this.aCarDetailOwnerInfo.ownerId = data.ownerId
      this._loadACarDetailOwnerData(1, this.pageSize)
    },
    handlePageChange(page) {
      this.currentPage = page
      this._loadACarDetailOwnerData(page, this.pageSize)
    },
    async _loadACarDetailOwnerData(page, row) {
      try {
        const param = {
          ownerId: this.aCarDetailOwnerInfo.ownerId,
          ownerTypeCd: '1001',
          page,
          row
        }
        const response = await queryAdminOwners(param)
        this.aCarDetailOwnerInfo.owners = response.data
        this.total = response.total
      } catch (error) {
        console.error('Failed to load owner data:', error)
      }
    },
    _viewOwnerFace(url) {
      if (url) {
        this.$refs.viewImage.showImage({ url })
      }
    },
    _toACarDetailOwnerDetail(owner) {
      window.open('/#/views/aCommunity/adminOwnerDetail?ownerId=' + owner.ownerId)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.text-right {
  text-align: right;
}

.border-radius {
  border-radius: 4px;
}
</style>