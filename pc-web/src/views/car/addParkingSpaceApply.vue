<template>
  <el-card class="box-card">
    <div slot="header" class=" flex justify-between">
      <div>{{ $t('addParkingSpaceApply.title') }}</div>
      <div>
        <el-button type="primary" size="small" icon="el-icon-close" @click="_goBack">
          {{ $t('common.back') }}
        </el-button>
      </div>
    </div>

    <el-form ref="form" :model="addParkingSpaceApplyInfo" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addParkingSpaceApply.carNum')" prop="carNum" :rules="[
            { required: true, message: $t('addParkingSpaceApply.requiredCarNum') },
            { pattern: /^[\u4e00-\u9fa5A-Za-z0-9]{6,12}$/, message: $t('addParkingSpaceApply.invalidCarNum') }
          ]">
            <el-input v-model="addParkingSpaceApplyInfo.carNum"
              :placeholder="$t('addParkingSpaceApply.placeholderCarNum')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addParkingSpaceApply.carBrand')" prop="carBrand" :rules="[
            { required: true, message: $t('addParkingSpaceApply.requiredCarBrand') },
            { max: 50, message: $t('addParkingSpaceApply.maxCarBrand') }
          ]">
            <el-input v-model="addParkingSpaceApplyInfo.carBrand"
              :placeholder="$t('addParkingSpaceApply.placeholderCarBrand')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addParkingSpaceApply.carType')" prop="carType"
            :rules="[{ required: true, message: $t('addParkingSpaceApply.requiredCarType') }]">
            <el-select v-model="addParkingSpaceApplyInfo.carType" style="width: 100%"
              :placeholder="$t('addParkingSpaceApply.placeholderCarType')">
              <el-option v-for="item in carTypes" :key="item.statusCd" :label="item.name" :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addParkingSpaceApply.carColor')" prop="carColor" :rules="[
            { required: true, message: $t('addParkingSpaceApply.requiredCarColor') },
            { max: 12, message: $t('addParkingSpaceApply.maxCarColor') }
          ]">
            <el-input v-model="addParkingSpaceApplyInfo.carColor"
              :placeholder="$t('addParkingSpaceApply.placeholderCarColor')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="$t('addParkingSpaceApply.applyPersonName')" prop="applyPersonName" :rules="[
            { required: true, message: $t('addParkingSpaceApply.requiredApplyPersonName') },
            { max: 64, message: $t('addParkingSpaceApply.maxApplyPersonName') }
          ]">
            <el-input v-model="addParkingSpaceApplyInfo.applyPersonName"
              :placeholder="$t('addParkingSpaceApply.placeholderApplyPersonName')"
              style="width: calc(100% - 70px); margin-right: 10px" />

          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" @click="_openChooseOwner">
            {{ $t('addParkingSpaceApply.selectOwner') }}
          </el-button>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addParkingSpaceApply.applyPersonLink')" prop="applyPersonLink" :rules="[
            { required: true, message: $t('addParkingSpaceApply.requiredApplyPersonLink') },
            { max: 11, message: $t('addParkingSpaceApply.maxApplyPersonLink') }
          ]">
            <el-input v-model="addParkingSpaceApplyInfo.applyPersonLink"
              :placeholder="$t('addParkingSpaceApply.placeholderApplyPersonLink')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('addParkingSpaceApply.remark')" prop="remark"
            :rules="[{ max: 300, message: $t('addParkingSpaceApply.maxRemark') }]">
            <el-input v-model="addParkingSpaceApplyInfo.remark" type="textarea"
              :placeholder="$t('addParkingSpaceApply.placeholderRemark')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24" style="text-align: right">
          <el-button type="warning" style="margin-right: 20px" @click="_goBack">
            {{ $t('common.back') }}
          </el-button>
          <el-button type="primary" @click="saveParkingSpaceApplyInfo">
            <i class="el-icon-check"></i>
            {{ $t('common.submit') }}
          </el-button>
        </el-col>
      </el-row>
    </el-form>

    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner" />
  </el-card>
</template>

<script>
import { saveParkingSpaceApply } from '@/api/car/addParkingSpaceApplyApi'
import { getDict } from '@/api/community/communityApi'
import SearchOwner from '@/components/owner/SearchOwner'

export default {
  name: 'AddParkingSpaceApply',
  components: {
    SearchOwner
  },
  data() {
    return {
      addParkingSpaceApplyInfo: {
        paName: '',
        paId: '',
        psId: '',
        psName: '',
        applyId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        applyPersonName: '',
        applyPersonLink: '',
        applyPersonId: '',
        state: '1001',
        remark: ''
      },
      carTypes: []
    }
  },
  async created() {
    await this.getCarTypeDict()
  },
  methods: {
    async getCarTypeDict() {
      try {
        const data = await getDict('owner_car', 'car_type')
        this.carTypes = data
      } catch (error) {
        console.error('获取车辆类型字典失败:', error)
      }
    },
    handleChooseOwner(owner) {
      this.addParkingSpaceApplyInfo.applyPersonName = owner.name
      this.addParkingSpaceApplyInfo.applyPersonLink = owner.link
      this.addParkingSpaceApplyInfo.applyPersonId = owner.ownerId
    },
    _openChooseOwner() {
      this.$refs.searchOwner.open()
    },
    async saveParkingSpaceApplyInfo() {
      try {
        await this.$refs.form.validate()

        const data = {
          ...this.addParkingSpaceApplyInfo,
          communityId: this.getCommunityId()
        }

        await saveParkingSpaceApply(data)
        this.$message.success(this.$t('common.operationSuccess'))
        this.clearAddParkingSpaceApplyInfo()
        this._goBack()
      } catch (error) {
        if (error.errors) {
          this.$message.error(Object.values(error.errors).join('; '))
        } else {
          console.error('保存失败:', error)
        }
      }
    },
    clearAddParkingSpaceApplyInfo() {
      this.addParkingSpaceApplyInfo = {
        paName: '',
        paId: '',
        psId: '',
        psName: '',
        applyId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        applyPersonName: '',
        applyPersonLink: '',
        applyPersonId: '',
        state: '1001',
        remark: ''
      }
    },
    _goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.box-card {
  margin: 20px;
}

.el-form-item {
  margin-bottom: 22px;
}
</style>