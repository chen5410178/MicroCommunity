<template>
  <div>
    <el-card class="box-card margin">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('hireParkingSpace.title') }}</span>
        <div class="">
          <el-button type="primary" size="small" icon="el-icon-close" @click="goBack">
            {{ $t('common.back') }}
          </el-button>
        </div>
      </div>

      <el-form ref="form" :model="hireParkingSpaceInfo" :rules="dynamicRules" label-width="120px"
        label-position="right">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.carNum')" prop="carNum">
              <el-input v-model="hireParkingSpaceInfo.carNum" :placeholder="$t('hireParkingSpace.carNumPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.carBrand')">
              <el-input v-model="hireParkingSpaceInfo.carBrand"
                :placeholder="$t('hireParkingSpace.carBrandPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.carType')" prop="carType">
              <el-select v-model="hireParkingSpaceInfo.carType" class="w-full"
                :placeholder="$t('hireParkingSpace.carTypePlaceholder')">
                <el-option v-for="carType in hireParkingSpaceInfo.carTypes" :key="carType.statusCd"
                  :label="carType.name" :value="carType.statusCd" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.carColor')">
              <el-input v-model="hireParkingSpaceInfo.carColor"
                :placeholder="$t('hireParkingSpace.carColorPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.licenseType')" prop="leaseType">
              <el-select v-model="hireParkingSpaceInfo.leaseType" class="w-full"
                :placeholder="$t('hireParkingSpace.licenseTypePlaceholder')" @change="changeLeaseType">
                <el-option :label="$t('hireParkingSpace.monthlyRent')" value="H" />
                <el-option :label="$t('hireParkingSpace.saleCar')" value="S" />
                <el-option :label="$t('hireParkingSpace.internalCar')" value="I" />
                <el-option :label="$t('hireParkingSpace.freeCar')" value="NM" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="showDateRange" :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.startTime')" prop="startTime">
              <el-date-picker v-model="hireParkingSpaceInfo.startTime" type="date" value-format="yyyy-MM-dd"
                class="w-full" :placeholder="$t('hireParkingSpace.startTimePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.endTime')" prop="endTime">
              <el-date-picker v-model="hireParkingSpaceInfo.endTime" type="date" value-format="yyyy-MM-dd"
                class="w-full" :placeholder="$t('hireParkingSpace.endTimePlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item :label="$t('hireParkingSpace.owner')" prop="ownerName">
              <el-input v-model="hireParkingSpaceInfo.ownerName" disabled
                :placeholder="$t('hireParkingSpace.ownerPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <div class="">
              <el-button type="primary" size="small" @click="openChooseOwner">
                {{ $t('common.select') }}
              </el-button>
            </div>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('hireParkingSpace.contact')" prop="contact">
              <el-input v-model="hireParkingSpaceInfo.contact"
                :placeholder="$t('hireParkingSpace.contactPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('hireParkingSpace.tel')" prop="tel">
              <el-input v-model="hireParkingSpaceInfo.tel"
                :placeholder="$t('hireParkingSpace.telPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('hireParkingSpace.parkingArea')" prop="paId">
              <el-select v-model="hireParkingSpaceInfo.paId" class="w-full"
                :placeholder="$t('hireParkingSpace.parkingAreaPlaceholder')" @change="handleParkingAreaChange">
                <el-option v-for="item in hireParkingSpaceInfo.parkingAreas" :key="item.paId" :label="item.num"
                  :value="item.paId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('hireParkingSpace.parkingSpace')" prop="psName">
              <el-input v-model="hireParkingSpaceInfo.psName" disabled
                :placeholder="$t('hireParkingSpace.parkingSpacePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="small" @click="openSearchParkingSpaceModel">
              {{ $t('common.select') }}
            </el-button>
          </el-col>
        </el-row>

        <el-row v-for="(item, index) in hireParkingSpaceInfo.attrs" :key="index">
          <el-col :span="24">
            <el-form-item :label="item.specName">
              <el-input v-if="item.specType === '2233'" v-model="item.value" :placeholder="item.specHoldplace" />
              <el-select v-else-if="item.specType === '3344'" v-model="item.value" class="w-full"
                :placeholder="item.specHoldplace">
                <el-option v-for="(val, idx) in item.values" :key="idx" :label="val.valueName" :value="val.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('hireParkingSpace.remark')">
              <el-input v-model="hireParkingSpaceInfo.remark" type="textarea" :rows="3"
                :placeholder="$t('hireParkingSpace.remarkPlaceholder')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <div class="mt-20 text-right">
      <el-button @click="goBack">{{ $t('common.back') }}</el-button>
      <el-button type="primary" @click="saveAddCarInfo">
        {{ $t('common.submit') }}
      </el-button>
    </div>

    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner" />
    <search-parking-space ref="searchParkingSpace" @choose-parking-space="handleChooseParkingSpace" />
  </div>
</template>

<script>
import { saveOwnerCar, listParkingAreas } from '@/api/car/hireParkingSpaceApi'
import { getCommunityId, getDict } from '@/api/community/communityApi'
import SearchOwner from '@/components/owner/SearchOwner'
import SearchParkingSpace from '@/components/car/SearchParkingSpace'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'

export default {
  name: 'HireParkingSpaceList',
  components: {
    SearchOwner,
    SearchParkingSpace
  },
  data() {
    return {
      hireParkingSpaceInfo: {
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        remark: '',
        startTime: '',
        endTime: '',
        leaseType: 'H',
        ownerId: '',
        ownerName: '',
        paId: '',
        psId: '',
        psName: '',
        contact: '',
        tel: '',
        attrs: [],
        carTypes: [],
        parkingAreas: []
      },
    }
  },
  computed: {
    showDateRange() {
      return ['H', 'S'].includes(this.hireParkingSpaceInfo.leaseType)
    },
    dynamicRules() {
      const baseRules = {
        carNum: [
          { required: true, message: this.$t('hireParkingSpace.validate.carNum'), trigger: 'blur' }
        ],
        carType: [
          { required: true, message: this.$t('hireParkingSpace.validate.carType'), trigger: 'change' }
        ],
        leaseType: [
          { required: true, message: this.$t('hireParkingSpace.validate.licenseType'), trigger: 'change' }
        ],
        ownerName: [
          { required: true, message: this.$t('hireParkingSpace.validate.owner'), trigger: 'change' }
        ],
        paId: [
          { required: true, message: this.$t('hireParkingSpace.validate.parkingArea'), trigger: 'change' }
        ],
        tel: [
          {
            validator: (rule, value, callback) => {
              if (value && !/^1[3-9]\d{9}$/.test(value)) {
                callback(new Error(this.$t('hireParkingSpace.validate.tel')))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }

      // 根据租赁类型动态添加时间验证规则
      if (this.showDateRange) {
        baseRules.startTime = [
          { required: true, message: this.$t('hireParkingSpace.validate.startTime'), trigger: 'change' }
        ]
        baseRules.endTime = [
          { required: true, message: this.$t('hireParkingSpace.validate.endTime'), trigger: 'change' }
        ]
      }

      return baseRules
    }
  },
  async created() {
    await this.initData()
    const { ownerId, ownerName } = this.$route.query
    if (ownerId) {
      this.hireParkingSpaceInfo.ownerId = ownerId
      this.hireParkingSpaceInfo.ownerName = ownerName
    }
  },
  methods: {
    async initData() {
      this.communityId = getCommunityId()

      // 加载停车场列表
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.communityId
        }
        const res = await listParkingAreas(params)
        this.hireParkingSpaceInfo.parkingAreas = res.data || res.parkingAreas || []
      } catch (error) {
        console.error('加载停车场失败:', error)
      }

      // 加载车辆类型字典
      try {
        const carTypes = await getDict('owner_car', 'car_type')
        this.hireParkingSpaceInfo.carTypes = carTypes
      } catch (error) {
        console.error('加载车辆类型失败:', error)
      }

      // 加载车辆属性规格
      try {
        await this.loadCarAttrSpec()
      } catch (error) {
        console.error('加载车辆属性失败:', error)
      }
    },

    async loadCarAttrSpec() {
      try {
        const {data} = await getAttrSpecList({
          page: 1,
          row: 100,
          tableName: 'owner_car_attr'
        })
        this.hireParkingSpaceInfo.attrs = data.map(item => {
          if (item.specShow === 'Y') {
            return {
              ...item,
              value: '',
              values: item.specType === '3344' ? [] : undefined
            }
          }
          return null
        }).filter(Boolean)
      } catch (error) {
        console.error('加载车辆属性规格失败:', error)
      }
    },

    changeLeaseType() {
      this.hireParkingSpaceInfo.startTime = ''
      this.hireParkingSpaceInfo.endTime = ''
      // 清除时间字段的验证状态
      this.$nextTick(() => {
        this.$refs.form.clearValidate(['startTime', 'endTime'])
      })
    },

    openChooseOwner() {
      this.$refs.searchOwner.open()
    },

    handleChooseOwner(owner) {
      this.hireParkingSpaceInfo.ownerName = owner.name
      this.hireParkingSpaceInfo.ownerId = owner.memberId
      // 默认带出业主姓名与联系电话，可手工修改
      this.hireParkingSpaceInfo.contact = owner.name
      this.hireParkingSpaceInfo.tel = owner.link
      // 触发表单验证
      this.$refs.form.validateField('ownerName')
    },

    openSearchParkingSpaceModel() {
      this.$refs.searchParkingSpace.open(this.hireParkingSpaceInfo.paId)
    },

    handleParkingAreaChange() {
      // 切换停车场后清空已选车位，避免与旧停车场不匹配
      this.hireParkingSpaceInfo.psId = ''
      this.hireParkingSpaceInfo.psName = ''
      this.$nextTick(() => {
        this.$refs.form.clearValidate(['psName'])
      })
    },

    handleChooseParkingSpace(parkingSpace) {
      // 校验所选车位是否属于已选停车场
      if (this.hireParkingSpaceInfo.paId && parkingSpace.paId && this.hireParkingSpaceInfo.paId !== parkingSpace.paId) {
        this.$message.warning(this.$t('hireParkingSpace.validate.parkingAreaMismatch'))
        return
      }
      this.hireParkingSpaceInfo.psName = `${parkingSpace.areaNum}-${parkingSpace.num}`
      this.hireParkingSpaceInfo.psId = parkingSpace.psId
      if (parkingSpace.paId) {
        this.hireParkingSpaceInfo.paId = parkingSpace.paId
      }
      // 触发表单验证
      this.$refs.form.validateField('psName')
      this.$refs.form.validateField('paId')
    },

    async saveAddCarInfo() {
      // 使用 Element UI 表单验证
      try {
        await this.$refs.form.validate()
      } catch (error) {
        return
      }

      // 验证自定义属性
      for (const attr of this.hireParkingSpaceInfo.attrs) {
        if (attr.required === 'Y' && !attr.value) {
          this.$message.error(attr.specHoldplace)
          return
        }
      }

      try {
        const params = {
          ...this.hireParkingSpaceInfo,
          communityId: this.communityId
        }

        await saveOwnerCar(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error(this.$t('hireParkingSpace.saveError'))
      }
    },

    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.card-header-actions {
  position: absolute;
  right: 20px;
  top: 15px;
}

.w-full {
  width: 100%;
}

.mt-10 {
  margin-top: 10px;
}

.mt-20 {
  margin-top: 20px;
}
</style>