<template>
  <div class="list-room-renovation-record-details-container">
    <el-row :gutter="20" style="width:100%">
      <el-col :span="24" style="width:100%">
        <el-card>
          <div slot="header" class="flex justify-between">
            <div>
              <span>{{ roomRenovationRecordDetailsInfo.conditions.roomName }}</span>
              <span>{{ $t('listRoomRenovationRecordDetails.title') }}</span>
            </div>
            <div class="header-tools">

              <el-button type="primary" size="small" @click="_goBack" icon="el-icon-close">
                {{ $t('listRoomRenovationRecordDetails.back') }}
              </el-button>
            </div>
          </div>

          <el-table :data="roomRenovationRecordDetailsInfo.roomRenovationRecordDetails" border style="width: 100%">
            <el-table-column prop="roomName" :label="$t('listRoomRenovationRecordDetails.room')" align="center" />
            <el-table-column :label="$t('listRoomRenovationRecordDetails.media')" align="center">
              <template slot-scope="scope">
                <div v-if="scope.row.relTypeCd === '19000'" class="image-container" >
                  <el-image :src="scope.row.url" :preview-src-list="[scope.row.url]" fit="cover"
                    style="width:50px;height:50px">
                    <div slot="error" class="image-slot">
                      <img src="/img/noPhoto.jpg" style="width:50px;height:50px">
                    </div>
                  </el-image>
                  <img src="/img/icon-bigimg.png" class="preview-icon">
                </div>
                <video v-else-if="scope.row.relTypeCd === '21000'" width="200" height="200" controls autoplay>
                  <source :src="scope.row.url" type="video/mp4">
                </video>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('listRoomRenovationRecordDetails.createTime')"
              align="center" />
          </el-table>

          <el-pagination :current-page.sync="pagination.current" :page-sizes="[10, 20, 30, 50]"
            :page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage" />
  </div>
</template>

<script>
import { queryRoomRenovationRecordDetail } from '@/api/community/listRoomRenovationRecordDetailsApi'
import ViewImage from '@/components/community/viewImage'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ListRoomRenovationRecordDetails',
  components: {
    ViewImage
  },
  data() {
    return {
      roomRenovationRecordDetailsInfo: {
        roomRenovationRecordDetails: [],
        conditions: {
          recordId: '',
          roomName: '',
          state: '',
          roomId: '',
          communityId: '',
          page: 1,
          row: 10
        }
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      loading: false
    }
  },
  created() {
    this.roomRenovationRecordDetailsInfo.conditions.communityId = getCommunityId()
    this.roomRenovationRecordDetailsInfo.conditions.recordId = this.$route.query.recordId
    this.roomRenovationRecordDetailsInfo.conditions.roomName = this.$route.query.roomName
    this.roomRenovationRecordDetailsInfo.conditions.state = this.$route.query.state
    this._listRoomRenovationRecordDetails()
  },
  methods: {
    async _listRoomRenovationRecordDetails() {
      try {
        this.loading = true
        const params = {
          ...this.roomRenovationRecordDetailsInfo.conditions,
          page: this.pagination.current,
          row: this.pagination.size
        }
        const { data, total } = await queryRoomRenovationRecordDetail(params)
        this.roomRenovationRecordDetailsInfo.roomRenovationRecordDetails = data
        this.pagination.total = total
      } catch (error) {
        this.$message.error(this.$t('listRoomRenovationRecordDetails.fetchError'))
      } finally {
        this.loading = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this._listRoomRenovationRecordDetails()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this._listRoomRenovationRecordDetails()
    },
    _goBack() {
      this.$router.go(-1)
    },
    showImg(url) {
      this.$refs.viewImage.open(url)
    }
  }
}
</script>

<style lang="scss" scoped>
.list-room-renovation-record-details-container {
  padding: 20px;

  .clearfix {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .header-tools {
    display: flex;
    align-items: center;
  }

  .image-container {
    position: relative;
    display: inline-block;
    cursor: pointer;

    .preview-icon {
      position: absolute;
      right: 0;
      bottom: 0;
      width: 20px;
      height: 20px;
    }
  }
}
</style>