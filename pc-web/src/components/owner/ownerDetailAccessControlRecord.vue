<template>
  <div class="margin-top">
    <el-table
      :data="ownerDetailAccessControlRecordInfo.inouts"
      style="width: 100%"
      border
      stripe
    >
      <el-table-column :label="$t('ownerDetailAccessControlRecord.face')" align="center" width="120">
        <template slot-scope="scope">
          <img 
            style="width: 60px; height: 60px;" 
            class="border-radius" 
            :src="scope.row.facePath || '/img/noPhoto.jpg'" 
            @click="_viewOwnerFace(scope.row.facePath)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="inoutId" :label="$t('ownerDetailAccessControlRecord.id')" align="center"></el-table-column>
      <el-table-column prop="machineName" :label="$t('ownerDetailAccessControlRecord.deviceName')" align="center"></el-table-column>
      <el-table-column prop="machineCode" :label="$t('ownerDetailAccessControlRecord.deviceCode')" align="center"></el-table-column>
      <el-table-column prop="name" :label="$t('ownerDetailAccessControlRecord.userName')" align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailAccessControlRecord.openType')" align="center">
        <template slot-scope="scope">
          {{scope.row.openTypeCd === '1000' ? $t('ownerDetailAccessControlRecord.face') : $t('ownerDetailAccessControlRecord.other')}}
        </template>
      </el-table-column>
      <el-table-column prop="tel" :label="$t('ownerDetailAccessControlRecord.phone')" align="center">
        <template slot-scope="scope">
          {{scope.row.tel || '-'}}
        </template>
      </el-table-column>
      <el-table-column prop="idCard" :label="$t('ownerDetailAccessControlRecord.idCard')" align="center">
        <template slot-scope="scope">
          {{scope.row.idCard || '-'}}
        </template>
      </el-table-column>
      <el-table-column prop="similar" :label="$t('ownerDetailAccessControlRecord.similarity')" align="center"></el-table-column>
      <el-table-column :label="$t('ownerDetailAccessControlRecord.openStatus')" align="center">
        <template slot-scope="scope">
          {{scope.row.state === 'C' ? $t('ownerDetailAccessControlRecord.success') : $t('ownerDetailAccessControlRecord.failed')}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" :label="$t('ownerDetailAccessControlRecord.openTime')" align="center"></el-table-column>
    </el-table>
    <el-row>
      <el-col :span="4"></el-col>
      <el-col :span="20">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-col>
    </el-row>
    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import { getOpenApi } from '@/api/owner/ownerDetailAccessControlRecordApi'
import { getCommunityId } from '@/api/community/communityApi'
import ViewImage from '@/components/system/viewImage'

export default {
  name: 'OwnerDetailAccessControlRecord',
  components: {
    ViewImage
  },
  data() {
    return {
      ownerDetailAccessControlRecordInfo: {
        inouts: [],
        ownerId: '',
        link: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(ownerId, link) {
      this.ownerDetailAccessControlRecordInfo.ownerId = ownerId
      this.ownerDetailAccessControlRecordInfo.link = link
      this._loadOwnerDetailAccessControlRecordData(this.currentPage, this.pageSize)
    },
    _loadOwnerDetailAccessControlRecordData(page, row) {
      const param = {
        page: page,
        row: row,
        communityId: this.communityId,
        tel: this.ownerDetailAccessControlRecordInfo.link,
        iotApiCode: 'listAccessControlInoutBmoImpl'
      }

      getOpenApi(param).then(response => {
        this.ownerDetailAccessControlRecordInfo.inouts = response.data.data
        this.total = response.data.total
      }).catch(error => {
        console.error('请求失败:', error)
      })
    },
    _viewOwnerFace(url) {
      if (url) {
        this.$refs.viewImage.show(url)
      }
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadOwnerDetailAccessControlRecordData(val, this.pageSize)
    }
  }
}
</script>