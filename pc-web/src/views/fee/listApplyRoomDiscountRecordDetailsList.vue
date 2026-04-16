<template>
  <div class="listApplyRoomDiscountRecordDetails-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ applyRoomDiscountRecordDetailsInfo.conditions.roomName }} {{
          $t('listApplyRoomDiscountRecordDetails.title') }}</span>
        <el-button type="primary" size="small" style="float: right;" @click="_goBack">
          <i class="el-icon-close"></i>
          {{ $t('listApplyRoomDiscountRecordDetails.back') }}
        </el-button>
      </div>

      <el-table :data="applyRoomDiscountRecordDetailsInfo.applyRoomDiscountRecordDetails" border style="width: 100%">
        <el-table-column prop="roomName" :label="$t('listApplyRoomDiscountRecordDetails.room')" align="center" />
        <el-table-column :label="$t('listApplyRoomDiscountRecordDetails.image')" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.relTypeCd === '19000'" style="position: relative; display: inline-block;"
              @click="showImg(scope.row.url)">
              <img width="50" height="50" :src="scope.row.url" @error="handleImageError">
              <img src="/img/icon-bigimg.png" style="position: absolute; right: 0; bottom: 0;" width="50" height="50"
                alt="">
            </div>
            <video v-else-if="scope.row.relTypeCd === '21000'" width="200" height="200" controls autoplay>
              <source :src="scope.row.url" type="video/mp4">
            </video>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('listApplyRoomDiscountRecordDetails.createTime')"
          align="center" />
      </el-table>

      <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.size"
        :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { queryApplyRoomDiscountRecordDetail } from '@/api/fee/listApplyRoomDiscountRecordDetailsApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'ListApplyRoomDiscountRecordDetails',
  components: {
    ViewImage
  },
  data() {
    return {
      applyRoomDiscountRecordDetailsInfo: {
        applyRoomDiscountRecordDetails: [],
        conditions: {
          ardrId: '',
          roomName: '',
          state: '',
          roomId: '',
          communityId: ''
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  created() {
    this.applyRoomDiscountRecordDetailsInfo.conditions.ardrId = this.$route.query.ardrId
    this.applyRoomDiscountRecordDetailsInfo.conditions.roomName = this.$route.query.roomName
    this.applyRoomDiscountRecordDetailsInfo.conditions.state = this.$route.query.state
    this.applyRoomDiscountRecordDetailsInfo.conditions.communityId = getCommunityId()
    this._listApplyRoomDiscountRecordDetails()
  },
  methods: {
    async _listApplyRoomDiscountRecordDetails() {
      try {
        const params = {
          page: this.pagination.current,
          row: this.pagination.size,
          ...this.applyRoomDiscountRecordDetailsInfo.conditions
        }
        const { data, total } = await queryApplyRoomDiscountRecordDetail(params)
        this.applyRoomDiscountRecordDetailsInfo.applyRoomDiscountRecordDetails = data
        this.pagination.total = total
      } catch (error) {
        console.error('Failed to fetch apply room discount record details:', error)
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listApplyRoomDiscountRecordDetails()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listApplyRoomDiscountRecordDetails()
    },
    _goBack() {
      this.$router.go(-1)
    },
    showImg(url) {
      this.$refs.viewImage.open(url)
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    }
  }
}
</script>

<style lang="scss" scoped>
.listApplyRoomDiscountRecordDetails-container {
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