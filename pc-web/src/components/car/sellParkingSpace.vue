<template>
  <el-dialog
    :title="$t('sellParkingSpace.title')"
    :visible.sync="visible"
    width="50%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('sellParkingSpace.sellOrHire')" prop="sellOrHire">
        <el-radio-group v-model="form.sellOrHire">
          <el-radio label="H">{{ $t('sellParkingSpace.hire') }}</el-radio>
          <el-radio label="S">{{ $t('sellParkingSpace.sell') }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item :label="$t('sellParkingSpace.parkingSpace')" prop="psId">
        <el-input :value="form.num" disabled></el-input>
      </el-form-item>

      <el-form-item :label="$t('sellParkingSpace.owner')" prop="ownerId">
        <el-select v-model="form.ownerId" filterable :placeholder="$t('sellParkingSpace.ownerPlaceholder')"
          style="width:100%">
          <el-option v-for="item in owners" :key="item.ownerId" :label="item.name + '(' + (item.link || '') + ')'"
            :value="item.ownerId"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('sellParkingSpace.carNum')" prop="carNum">
        <el-input v-model="form.carNum" :placeholder="$t('sellParkingSpace.carNumPlaceholder')"></el-input>
      </el-form-item>

      <template v-if="form.carNum">
        <el-form-item :label="$t('sellParkingSpace.carBrand')">
          <el-input v-model="form.carBrand" :placeholder="$t('sellParkingSpace.carBrandPlaceholder')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('sellParkingSpace.carType')">
          <el-select v-model="form.carType" :placeholder="$t('sellParkingSpace.carTypePlaceholder')" style="width:100%">
            <el-option :label="$t('sellParkingSpace.carType9901')" value="9901"></el-option>
            <el-option :label="$t('sellParkingSpace.carType9902')" value="9902"></el-option>
            <el-option :label="$t('sellParkingSpace.carType9903')" value="9903"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('sellParkingSpace.carColor')">
          <el-input v-model="form.carColor" :placeholder="$t('sellParkingSpace.carColorPlaceholder')"></el-input>
        </el-form-item>
      </template>

      <template v-if="form.sellOrHire === 'H'">
        <el-form-item :label="$t('sellParkingSpace.startTime')" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('sellParkingSpace.startTimePlaceholder')"
            style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('sellParkingSpace.endTime')" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('sellParkingSpace.endTimePlaceholder')"
            style="width:100%"></el-date-picker>
        </el-form-item>
      </template>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('sellParkingSpace.cancel') }}</el-button>
      <el-button type="primary" @click="sellParkingSpace" :loading="saving">
        {{ $t('sellParkingSpace.submit') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { sellParkingSpace } from '@/api/car/listParkingSpaceApi'
import { queryOwners } from '@/api/owner/ownerApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'SellParkingSpace',
  data() {
    return {
      visible: false,
      saving: false,
      owners: [],
      form: {
        psId: '',
        num: '',
        ownerId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        sellOrHire: 'H',
        startTime: '',
        endTime: '',
        storeId: '',
        communityId: getCommunityId()
      },
      rules: {
        sellOrHire: [
          { required: true, message: this.$t('sellParkingSpace.sellOrHireRequired'), trigger: 'change' }
        ],
        ownerId: [
          { required: true, message: this.$t('sellParkingSpace.ownerRequired'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('sellParkingSpace.startTimeRequired'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('sellParkingSpace.endTimeRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        psId: row.psId || '',
        num: row.num || '',
        ownerId: '',
        carNum: '',
        carBrand: '',
        carType: '',
        carColor: '',
        sellOrHire: row.sellOrHire || 'H',
        startTime: '',
        endTime: '',
        storeId: '',
        communityId: getCommunityId()
      }
      this.visible = true
      this.loadOwners()
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    async loadOwners() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: getCommunityId()
        }
        const res = await queryOwners(params)
        this.owners = res.data || []
      } catch (error) {
        console.error('加载业主列表失败:', error)
      }
    },
    handleClose() {
      this.visible = false
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    validateForm() {
      return new Promise((resolve) => {
        this.$refs.form.validate((valid) => {
          resolve(valid)
        })
      })
    },
    async sellParkingSpace() {
      const isValid = await this.validateForm()
      if (!isValid) return

      if (this.form.sellOrHire === 'H') {
        if (!this.form.startTime || !this.form.endTime) {
          this.$message.warning(this.$t('sellParkingSpace.timeRequired'))
          return
        }
        if (new Date(this.form.endTime) <= new Date(this.form.startTime)) {
          this.$message.error(this.$t('sellParkingSpace.timeError'))
          return
        }
      }

      try {
        this.saving = true
        const params = { ...this.form }
        delete params.num
        await sellParkingSpace(params)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        console.error('出租/出售车位失败:', error)
        this.$message.error(error.message || this.$t('sellParkingSpace.errorMessage'))
      } finally {
        this.saving = false
      }
    }
  }
}
</script>
