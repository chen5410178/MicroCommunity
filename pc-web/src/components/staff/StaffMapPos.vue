<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>{{ $t('staffDetailInfo.staffTrack') }}</span>
    </div>
    <div id="wlLineMap" style="height: 400px;"></div>
  </el-card>
</template>

<script>
import { listWorkLicensePos } from '@/api/staff/staffDetailApi'
import WorkLicenseLineMapApi from '@/api/map/workLicenseLineMapApi'

export default {
  name: 'StaffMapPos',
  props: {
    staffId: {
      type: String,
      required: true
    }
  },
  mounted() {
    this.loadWorkLicensePos()
  },
  methods: {
    async loadWorkLicensePos() {
      try {
        const uploadDate = this.formatDate(new Date())
        const params = {
          page: 1,
          row: 1000,
          staffId: this.staffId,
          uploadDate: uploadDate
        }
        
        const res = await listWorkLicensePos(params)
        if (res.code === 0) {
          const pos = res.data.map(item => ({
            lon: item.lat,
            lat: item.lon
          }))
          
          const map = new WorkLicenseLineMapApi()
          map.initMap(pos, 'wlLineMap')
        }
      } catch (error) {
        console.error('Failed to load work license positions:', error)
      }
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>