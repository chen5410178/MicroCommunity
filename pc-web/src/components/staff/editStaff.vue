<!-- components/staff/editStaff.vue -->
<template>
  <el-dialog :title="$t('staff.modifyStaff')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="editStaffInfo" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('staff.name')" prop="username">
            <el-input v-model="editStaffInfo.username" :placeholder="$t('staff.requiredName')" />
          </el-form-item>
          <el-form-item :label="$t('email')" prop="email">
            <el-input v-model="editStaffInfo.email" :placeholder="$t('staff.optionalEmail')" />
          </el-form-item>
          <el-form-item :label="$t('staff.phone')" prop="tel">
            <el-input v-model="editStaffInfo.tel" :placeholder="$t('staff.requiredPhone')" />
          </el-form-item>
          <el-form-item :label="$t('staff.gender')" prop="sex">
            <el-select v-model="editStaffInfo.sex" :placeholder="$t('staff.requiredGender')" style="width: 100%">
              <el-option value="0" :label="$t('staff.male')" />
              <el-option value="1" :label="$t('staff.female')" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('staff.relCd')" prop="relCd">
            <el-select v-model="editStaffInfo.relCd" :placeholder="$t('staff.relCdPlaceholder')" style="width: 100%">
              <el-option v-for="(item,index) in editStaffInfo.relCds" :key="index" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('staff.address')" prop="address">
            <el-input v-model="editStaffInfo.address" :placeholder="$t('staff.requiredAddress')" />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="text-align: center">
          <el-image style="width: 200px; height: 150px; border: 1px dashed #ccc;"
            :src="editStaffInfo.photoUrl || '/img/noPhoto.jpg'" fit="cover" />
          <el-upload class="mt-10" :show-file-list="false" :before-upload="beforeUpload" :http-request="uploadImage" action="">
            <el-button size="small" type="primary">
              {{ $t('common.upload') }}
            </el-button>
          </el-upload>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('staff.cancel') }}</el-button>
      <el-button type="primary" @click="editStaffSubmit">{{ $t('staff.edit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { modifyStaff } from '@/api/staff/staffApi'
import { getDict } from '../../api/community/communityApi'
import { uploadImage } from '@/api/owner/ownerApi'


export default {
  name: 'EditStaff',
  props: {
    staffInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      visible: false,
      loading: false,
      editStaffInfo: {
        userId: '',
        username: '',
        email: '',
        tel: '',
        sex: '',
        address: '',
        photo: '',
        photoUrl: '',
        relCd: '',
        relCds: [],
        branchOrgs: [],
        departmentOrgs: [],
        parentOrgId: '',
        parentOrgName: '',
        parentTwoOrgId: '',
        orgNewName: '',
        orgId: ''
      }
    }
  },
  watch: {
    staffInfo: {
      immediate: true,
      handler(val) {
        if (val) {
          this.editStaffInfo = {
            ...val,
            username: val.name,
          }
        }
      }
    }
  },
  methods: {
    open(row) {
      let _url =  '/img/noPhoto.jpg'
      if(row.urls && row.urls.length > 0){
        _url = row.urls[0]
      }
      this.editStaffInfo = {
        ...row,
        username: row.name,
        photo: _url
      }
      this.editStaffInfo.photoUrl = row.photoUrl || '/img/noPhoto.jpg'
      console.log(this.editStaffInfo.photoUrl)
      this.getRelCd()
      this.visible = true
      
    },
    async getRelCd() {
      const data = await getDict('u_org_staff_rel', "rel_cd")
      this.editStaffInfo.relCds = data
      this.$forceUpdate()
    },
    handleClose() {
      this.visible = false
    },
    validateForm() {
      if (!this.editStaffInfo.username) {
        this.$message.error(this.$t('staff.requiredName'))
        return false
      }
      if (!this.editStaffInfo.tel) {
        this.$message.error(this.$t('staff.requiredPhone'))
        return false
      }
      if (this.editStaffInfo.sex === '') {
        this.$message.error(this.$t('staff.requiredGender'))
        return false
      }
      if (!this.editStaffInfo.address) {
        this.$message.error(this.$t('staff.requiredAddress'))
        return false
      }
      return true
    },
    async editStaffSubmit() {
      if (!this.validateForm()) return

      try {
        this.loading = true
        const data = {
          ...this.editStaffInfo,
          name: this.editStaffInfo.username,
          staffId: this.editStaffInfo.userId
        }

        await modifyStaff(data)
        this.visible = false
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.handleClose()
      } catch (error) {
        this.$message.error(error.message)
      } finally {
        this.loading = false
      }
    },
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error(this.$t('common.jpgOnly'))
      }
      if (!isLt2M) {
        this.$message.error(this.$t('common.sizeLimit'))
      }
      return isJPG && isLt2M
    },
    async uploadImage({ file }) {
      try {
        const res = await uploadImage({ file })
        this.editStaffInfo.photo = res.fileId
        this.editStaffInfo.photoUrl = res.url
        this.$forceUpdate()
      } catch (error) {
        this.$message.error(this.$t('listOwner.upload.failed'))
      }
    },
  }
}
</script>
