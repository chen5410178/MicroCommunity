<template>
  <div class="city-area-container">
    <el-row :gutter="20">
      <el-col :span="4" class="padding-r-0">
        <city-area-tree @switchCityAreaTree="handleSwitchCityAreaTree" @handleRefresh="handleRefresh" />
      </el-col>
      <el-col :span="20">
        <el-card class="box-card">
          <div slot="header" class="flex justify-between">
            <span>{{ $t('cityArea.detailTitle') }}</span>
          </div>
          <div class="margin-top">
            <el-row>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.areaName') }}
                  </label>
                  <label class="">{{ cityAreaInfo.areaName }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.areaCode') }}
                  </label>
                  <label class="">{{ cityAreaInfo.areaCode || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.areaLevel') }}
                  </label>
                  <label class="" v-if="cityAreaInfo.areaLevel == '101'">{{ $t('cityArea.province') }}</label>
                  <label class="" v-else-if="cityAreaInfo.areaLevel == '202'">{{ $t('cityArea.city') }}</label>
                  <label class="" v-else-if="cityAreaInfo.areaLevel == '303'">{{ $t('cityArea.district') }}</label>
                  <label class="" v-else>{{ $t('cityArea.area') }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.parentAreaCode') }}
                  </label>
                  <label class="">{{ cityAreaInfo.parentAreaCode || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.parentAreaName') }}
                  </label>
                  <label class="">{{ cityAreaInfo.parentAreaName || '-' }}</label>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="form-group">
                  <label class="col-form-label">
                    {{ $t('cityArea.createTime') }}
                  </label>
                  <label class="">{{ cityAreaInfo.createTime || '-' }}</label>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import CityAreaTree from '@/components/community/cityAreaTree'

import { getAreaDetail } from '@/api/community/cityAreaApi'

export default {
  name: 'CityAreaList',
  components: {
    CityAreaTree,
  },
  data() {
    return {
      cityAreaInfo: {
        id: '',
        areaCode: '',
        areaName: '',
        areaLevel: '',
        parentAreaCode: '',
        parentAreaName: '',
        createTime: ''
      }
    }
  },
  methods: {
    handleSwitchCityAreaTree(params) {
      this.cityAreaInfo.id = params.id
      this.cityAreaInfo.areaCode = params.areaCode
      this.cityAreaInfo.areaName = params.areaName
      this.getAreaDetail()
    },
    async getAreaDetail() {
      try {
        const params = {
          id: this.cityAreaInfo.id,
          page: 1,
          row: 1
        }
        const { areas } = await getAreaDetail(params)
        if (areas && areas.length > 0) {
          this.cityAreaInfo = { ...this.cityAreaInfo, ...areas[0] }
        }
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    handleRefresh() {
      this.getAreaDetail()
    }
  }
}
</script>

<style scoped>
.city-area-container {
  padding: 20px;
}

.padding-r-0 {
  padding-right: 0 !important;
}

.margin-top {
  margin-top: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.col-form-label {
  margin-bottom: 5px;
}
</style>