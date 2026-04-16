<template>
  <div class="owner-detail-visit">
    <div class="margin-top">
      <el-table :data="aOwnerDetailVisitInfo.visits" border style="width: 100%">
        <el-table-column prop="vId" :label="$t('aOwnerDetailVisit.visitorId')" align="center" />
        <el-table-column :label="$t('aOwnerDetailVisit.photo')" align="center">
          <template #default="{ row }">
            <div style="position: relative; display: inline-block;"
              @click="showVisitImg(row.url ? row.url : '/img/noPhoto.jpg')">
              <img width="50" height="50" :src="row.url ? row.url : '/img/noPhoto.jpg'" @error="handleImageError">
              <img src="/img/icon-bigimg.png" style="position: absolute;right: 0;bottom: 0;" width="50" height="50"
                alt="">
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('aOwnerDetailVisit.visitor')" align="center">
          <template #default="{ row }">
            {{ row.vName }}({{ row.phoneNumber }})/{{ row.visitGender === '0' ? $t('common.male') : $t('common.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="ownerName" :label="$t('aOwnerDetailVisit.ownerName')" align="center" />
        <el-table-column :label="$t('aOwnerDetailVisit.visitReason')" align="center">
          <template #default="{ row }">
            {{ row.visitCase }}({{ row.reasonTypeName }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('aOwnerDetailVisit.carNum')" align="center">
          <template #default="{ row }">
            <div v-if="row.carState === '1' && row.psId != null && row.psId !== '' && row.psId !== undefined">
              {{ row.carNum }}<br />({{ row.parkAreaNum }}{{ $t('aOwnerDetailVisit.parkingLot') }}-{{ row.parkingSpaceNum }}{{ $t('aOwnerDetailVisit.parkingSpace') }})
            </div>
            <div v-else>{{ row.carNum }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="entourage" :label="$t('aOwnerDetailVisit.entourage')" align="center" />
        <el-table-column prop="createTime" :label="$t('aOwnerDetailVisit.createTime')" align="center" />
        <el-table-column :label="$t('aOwnerDetailVisit.visitTime')" align="center">
          <template #default="{ row }">
            {{ row.visitTime }}<br />{{ row.departureTime }}
          </template>
        </el-table-column>
        <el-table-column prop="stateName" :label="$t('aOwnerDetailVisit.visitorStatus')" align="center" />
        <el-table-column prop="carStateName" :label="$t('aOwnerDetailVisit.carStatus')" align="center" />
        <el-table-column :label="$t('aOwnerDetailVisit.operation')" align="center" />
      </el-table>

      <el-row class="margin-top">
        <el-col :span="16"></el-col>
        <el-col :span="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listAdminVisits } from '@/api/aCommunity/aOwnerDetailVisitApi'
export default {
  name: 'AOwnerDetailVisit',
  data() {
    return {
      DEFAULT_PAGE: 1,
      DEFAULT_ROWS: 10,
      aOwnerDetailVisitInfo: {
        visits: [],
        ownerId: '',
        link: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  methods: {
    open(data) {
      this.aOwnerDetailVisitInfo.ownerId = data.ownerId
      this.aOwnerDetailVisitInfo.link = data.link
      this._loadAOwnerDetailVisitData(this.DEFAULT_PAGE, this.DEFAULT_ROWS)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadAOwnerDetailVisitData(val, this.DEFAULT_ROWS)
    },
    async _loadAOwnerDetailVisitData(page, row) {
      const param = {
        page,
        row,
        ownerTel: this.aOwnerDetailVisitInfo.link,
        channel: 'PC'
      }

      try {
        const response = await listAdminVisits(param)
        this.aOwnerDetailVisitInfo.visits = response.visits
        this.total = response.total
        this.currentPage = page
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    showVisitImg(url) {
      if (!url) {
        url = '/img/noPhoto.jpg'
      }
      this.$emit('viewImage', url)
    },
    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
    },
    _openVisitDetail(item) {
      const flowId = item.flowId || ''
      this.$router.push(`/pages/property/visitDetail?vId=${item.vId}&flowId=${flowId}`)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}
</style>