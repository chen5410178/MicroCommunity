<template>
  <el-dialog :title="$t('propertyCompanyManage.add')" :visible="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px" class="text-left">
      <el-form-item :label="$t('propertyCompanyManage.name')" prop="name">
        <el-input v-model="formData.name" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.address')" prop="address">
        <el-input v-model="formData.address" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.tel')" prop="tel">
        <el-input v-model="formData.tel" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.corporation')" prop="corporation">
        <el-input v-model="formData.corporation" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.foundingTime')" prop="foundingTime">
        <el-date-picker v-model="formData.foundingTime" type="date" value-format="yyyy-MM-dd" style="width: 100%" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.landmark')" prop="nearByLandmarks">
        <el-input v-model="formData.nearByLandmarks" />
      </el-form-item>
      <el-form-item :label="$t('propertyCompanyManage.communities')">
        <el-select v-model="formData.communityIds" multiple filterable style="width: 100%"
          :placeholder="$t('propertyCompanyManage.selectCommunities')">
          <el-option v-for="item in communities" :key="item.communityId" :label="item.name" :value="item.communityId" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="formData.communityIds && formData.communityIds.length > 0"
        :label="$t('propertyCompanyManage.functions')">
        <el-checkbox v-model="formData.isAll" @change="handleCheckAll">
          {{ $t('propertyCompanyManage.all') }}
        </el-checkbox>
        <el-checkbox-group v-model="formData.groupIds" @change="handleCheckGroup">
          <el-checkbox v-for="group in menuGroups" :key="group.gId" :label="group.gId">
            {{ group.name }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveProperty, listMenuGroup, listNoEnterCommunity } from '@/api/store/propertyCompanyManageApi'

export default {
  name: 'AddPropertyCompany',
  data() {
    return {
      visible: false,
      rules: {
        name: [
          { required: true, message: this.$t('propertyCompanyManage.validate.name'), trigger: 'blur' }
        ],
        address: [
          { required: true, message: this.$t('propertyCompanyManage.validate.address'), trigger: 'blur' }
        ],
        tel: [
          { required: true, message: this.$t('propertyCompanyManage.validate.tel'), trigger: 'blur' }
        ],
        corporation: [
          { required: true, message: this.$t('propertyCompanyManage.validate.corporation'), trigger: 'blur' }
        ],
        foundingTime: [
          { required: true, message: this.$t('propertyCompanyManage.validate.foundingTime'), trigger: 'blur' }
        ],
        nearByLandmarks: [
          { required: true, message: this.$t('propertyCompanyManage.validate.nearbyLandmarks'), trigger: 'blur' }
        ]
      },
      formData: {
        name: '',
        address: '',
        tel: '',
        corporation: '',
        foundingTime: '',
        nearByLandmarks: '',
        communityIds: [],
        menuGroups: [],
        groupIds: [],
        isAll: true
      },
      communities: [],
      menuGroups: []
    }
  },
  methods: {
    open() {
      this.visible = true
      this.fetchMenuGroups()
      this.fetchCommunities()
    },
    async fetchMenuGroups() {
      try {
        const { data } = await listMenuGroup({
          page: 1,
          row: 100,
          storeType: '800900000003'
        })
        this.menuGroups = data
        this.formData.groupIds = data.map(item => item.gId)
      } catch (error) {
        this.$message.error(this.$t('propertyCompanyManage.fetchMenuError'))
      }
    },
    async fetchCommunities() {
      try {
        const data = await listNoEnterCommunity({
          communityName: ''
        })
        this.communities = data
      } catch (error) {
        this.$message.error(this.$t('propertyCompanyManage.fetchCommunityError'))
      }
    },
    handleCheckAll(val) {
      this.formData.groupIds = val ? this.menuGroups.map(item => item.gId) : []
    },
    handleCheckGroup() {
      this.formData.isAll = this.formData.groupIds.length === this.menuGroups.length
    },
    async handleSubmit() {
      try {
        await saveProperty(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    handleClose() {
      this.visible = false
      this.$refs.form.resetFields()
      this.formData = {
        name: '',
        address: '',
        tel: '',
        corporation: '',
        foundingTime: '',
        nearByLandmarks: '',
        communityIds: [],
        menuGroups: [],
        groupIds: [],
        isAll: true
      }
    }
  }
}
</script>