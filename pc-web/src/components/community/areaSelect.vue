<template>
  <div class="area-select">
    <el-select v-model="areaSelectInfo.selectProv" :placeholder="$t('areaSelect.province')"
      style="width: 30%; margin-right: 10px" @change="getProv(areaSelectInfo.selectProv)">
      <el-option v-for="item in areaSelectInfo.provs" :key="item.areaCode" :label="item.areaName"
        :value="item.areaCode" />
    </el-select>
    <el-select v-model="areaSelectInfo.selectCity" :placeholder="$t('areaSelect.city')"
      style="width: 30%; margin-right: 10px" :disabled="!areaSelectInfo.selectProv"
      @change="getCity(areaSelectInfo.selectCity)">
      <el-option v-for="item in areaSelectInfo.citys" :key="item.areaCode" :label="item.areaName"
        :value="item.areaCode" />
    </el-select>
    <el-select v-model="areaSelectInfo.selectArea" :placeholder="$t('areaSelect.area')" style="width: 30%"
      :disabled="!areaSelectInfo.selectCity" @change="getArea(areaSelectInfo.selectArea)">
      <el-option v-for="item in areaSelectInfo.areas" :key="item.areaCode" :label="item.areaName"
        :value="item.areaCode" />
    </el-select>
  </div>
</template>

<script>
import { listAreas } from '@/api/community/communityManageApi'

export default {
  name: 'AreaSelect',
  props: {
    callBackListener: {
      type: String,
      default: ''
    },
    callBackFunction: {
      type: String,
      default: ''
    },
    namespace: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      areaSelectInfo: {
        areas: [],
        provs: [],
        citys: [],
        allCity: [],
        selectProv: '',
        selectProvName: '',
        selectCity: '',
        selectCityName: '',
        selectArea: '',
        selectAreaName: ''
      }
    }
  },
  created() {
    this._initArea('101', '0')
  },
  methods: {
    getProv(prov) {
      this._initArea('202', prov)
    },
    getCity(city) {
      this._initArea('303', city)
    },
    getArea(area) {
      this.areaSelectInfo.cityCode = area
      this.areaSelectInfo.areaAddress = ''
      if (!this.areaSelectInfo.provs) return

      this.areaSelectInfo.provs.forEach(param => {
        if (param.areaCode === this.areaSelectInfo.selectProv) {
          this.areaSelectInfo.selectProvName = param.areaName
        }
      })
      this.areaSelectInfo.citys.forEach(param => {
        if (param.areaCode === this.areaSelectInfo.selectCity) {
          this.areaSelectInfo.selectCityName = param.areaName
        }
      })
      this.areaSelectInfo.areas.forEach(param => {
        if (param.areaCode === this.areaSelectInfo.selectArea) {
          this.areaSelectInfo.selectAreaName = param.areaName
        }
      })

      this.$emit('selectArea', {
        selectProv: this.areaSelectInfo.selectProv,
        selectProvName: this.areaSelectInfo.selectProvName,
        selectCity: this.areaSelectInfo.selectCity,
        selectCityName: this.areaSelectInfo.selectCityName,
        selectArea: this.areaSelectInfo.selectArea,
        selectAreaName: this.areaSelectInfo.selectAreaName
      })
    },
    _initArea(areaLevel, parentAreaCode) {
      listAreas({ areaLevel, parentAreaCode }).then(res => {
        const tmpAreas = res.areas
        if (areaLevel === '101') {
          this.areaSelectInfo.provs = tmpAreas
        } else if (areaLevel === '202') {
          this.areaSelectInfo.citys = tmpAreas
        } else {
          this.areaSelectInfo.areas = tmpAreas
        }
      }).catch(error => {
        console.error('查询地区失败', error)
        this.$message.error(this.$t('areaSelect.fetchError'))
      })
    },
    clearArea() {
      this.areaSelectInfo.selectProv = ''
      this.areaSelectInfo.selectCity = ''
      this.areaSelectInfo.selectArea = ''
    }
  }
}
</script>