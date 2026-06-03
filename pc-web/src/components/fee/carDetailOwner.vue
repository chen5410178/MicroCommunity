<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="carDetailOwnerInfo.owners" border stripe>
        <el-table-column :label="$t('carDetailOwner.ownerFace')" align="center">
          <template slot-scope="scope">
            <el-image 
              style="width: 60px; height: 60px; border-radius: 4px;"
              :src="scope.row.url || '/img/noPhoto.jpg'"
              :preview-src-list="[scope.row.url]"
              v-if="scope.row.url"
            ></el-image>
            <el-image 
              style="width: 60px; height: 60px; border-radius: 4px;"
              src="/img/noPhoto.jpg"
              v-else
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailOwner.name')" align="center">
          <template slot-scope="scope">
            {{scope.row.name}}({{scope.row.link}})
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailOwner.gender')" align="center">
          <template slot-scope="scope">
            {{scope.row.sex == 0 ? $t('carDetailOwner.male') : $t('carDetailOwner.female')}}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('carDetailOwner.idCard')" align="center">
          <template slot-scope="scope">
            {{scope.row.idCard || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('carDetailOwner.address')" align="center">
          <template slot-scope="scope">
            {{scope.row.address || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="roomCount" :label="$t('carDetailOwner.roomCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.roomCount || 0}}
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" :label="$t('carDetailOwner.memberCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.memberCount || 0}}
          </template>
        </el-table-column>
        <el-table-column prop="carCount" :label="$t('carDetailOwner.carCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.carCount || 0}}
          </template>
        </el-table-column>
        <el-table-column prop="complaintCount" :label="$t('carDetailOwner.complaintCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.complaintCount || 0}}
          </template>
        </el-table-column>
        <el-table-column prop="repairCount" :label="$t('carDetailOwner.repairCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.repairCount || 0}}
          </template>
        </el-table-column>
        <el-table-column prop="oweFee" :label="$t('carDetailOwner.oweFee')" align="center">
          <template slot-scope="scope">
            {{scope.row.oweFee || '0.00'}}
          </template>
        </el-table-column>
        <el-table-column prop="contractCount" :label="$t('carDetailOwner.contractCount')" align="center">
          <template slot-scope="scope">
            {{scope.row.contractCount || 0}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('carDetailOwner.actions')" align="center">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_toCarDetailOwnerDetail(scope.row)">
                {{ $t('carDetailOwner.detail') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        :total="total"
        class="text-right"
        style="margin-top: 20px">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwners } from '@/api/fee/carDetailOwnerApi'

export default {
  name: 'CarDetailOwner',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      carDetailOwnerInfo: {
        owners: [],
        ownerId: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this._initEvent()
  },
  methods: {
    _initEvent() {
      this.$on('switch', this.handleSwitch)
      this.$on('notify', this._loadCarDetailOwnerData)
    },
    handleSwitch(data) {
      this.carDetailOwnerInfo.ownerId = data.ownerId
      this._loadCarDetailOwnerData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this._loadCarDetailOwnerData(val, this.DEFAULT_ROWS)
    },
    async _loadCarDetailOwnerData(page, row) {
      try {
        const res = await queryOwners({
          ownerId: this.carDetailOwnerInfo.ownerId,
          communityId: getCommunityId(),
          ownerTypeCd: '1001',
          page,
          row
        })
        this.carDetailOwnerInfo.owners = res.data
        this.total = res.total
      } catch (error) {
        console.error('Request failed:', error)
      }
    },
    _toCarDetailOwnerDetail(owner) {
      window.open(`/#/views/owner/ownerDetail?ownerId=${owner.ownerId}`)
    },
    open(params) {
      this.handleSwitch(params)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.text-right {
  text-align: right;
}
</style>