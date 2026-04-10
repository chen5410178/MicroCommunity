<!-- views/staff/addStaffList.vue -->
<template>
  <div class="add-staff-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ $t('addStaff.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form label-width="120px">
            <el-form-item :label="$t('addStaff.staffName')">
              <el-input v-model="addStaffInfo.username" :placeholder="$t('addStaff.staffNamePlaceholder')"
                maxlength="10" show-word-limit />
            </el-form-item>
            <el-form-item :label="$t('addStaff.staffGender')">
              <el-select v-model="addStaffInfo.sex" :placeholder="$t('addStaff.staffGenderPlaceholder')"
                style="width: 100%">
                <el-option :label="$t('addStaff.male')" value="0" />
                <el-option :label="$t('addStaff.female')" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('addStaff.relCd')">
              <el-select v-model="addStaffInfo.relCd" :placeholder="$t('addStaff.relCdPlaceholder')"
                style="width: 100%">
                <el-option v-for="item in addStaffInfo.relCds" :key="item.value" :label="item.name"
                  :value="item.statusCd" />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('addStaff.phone')">
              <el-input v-model="addStaffInfo.tel" :placeholder="$t('addStaff.phonePlaceholder')" maxlength="11" />
            </el-form-item>
            <el-form-item :label="$t('addStaff.photo')">
              <upload-image-url ref="uploadImage" :image-count="1" @notifyUploadCoverImage="handleUploadImage" />
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12">
          <el-form label-width="120px">
            <el-form-item :label="$t('addStaff.email')">
              <el-input v-model="addStaffInfo.email" :placeholder="$t('addStaff.emailPlaceholder')" />
            </el-form-item>
            <el-form-item :label="$t('addStaff.address')">
              <el-input v-model="addStaffInfo.address" :placeholder="$t('addStaff.addressPlaceholder')" />
            </el-form-item>
            <el-form-item :label="$t('addStaff.relatedOrg')">
              <el-input v-model="addStaffInfo.orgName" :placeholder="$t('addStaff.relatedOrgPlaceholder')" readonly
                @focus="handleChooseOrg" />
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <div class="footer-buttons">
        <el-button type="primary" @click="saveStaffInfo">
          <i class="el-icon-check" /> {{ $t('addStaff.save') }}
        </el-button>
        <el-button @click="$router.go(-1)">
          {{ $t('addStaff.cancel') }}
        </el-button>
      </div>
    </el-card>
    <choose-org-tree ref="chooseOrgTree" @switchOrg="handleSwitchOrg" />
  </div>
</template>

<script>
import { addStaff } from '@/api/staff/addStaffApi'
import UploadImageUrl from '@/components/upload/UploadImageUrl'
import ChooseOrgTree from '@/components/org/ChooseOrgTree'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AddStaff',
  components: {
    UploadImageUrl,
    ChooseOrgTree
  },
  data() {
    return {
      addStaffInfo: {
        orgId: '',
        orgName: '',
        relCd: '',
        username: '',
        sex: '',
        email: '',
        tel: '',
        address: '',
        photo: '',
        adminFlag: 'N',
        relCds: []
      }
    }
  },
  mounted() {
    this.getRelCd()
  },
  methods: {
    async getRelCd() {
      const data = await getDict('u_org_staff_rel', "rel_cd")
      this.addStaffInfo.relCds = data
    },
    handleUploadImage(data) {
      if (data.length > 0) {
        this.addStaffInfo.photo = data[0]
      }
    },
    handleChooseOrg() {
      this.$refs.chooseOrgTree.open()
    },
    handleSwitchOrg(org) {
      console.log(org)
      this.addStaffInfo.orgId = org.orgId
      this.addStaffInfo.orgName = org.allOrgName
    },
    validateForm() {
      if (!this.addStaffInfo.username || this.addStaffInfo.username.length < 2 || this.addStaffInfo.username.length > 10) {
        this.$message.error(this.$t('addStaff.staffNamePlaceholder'))
        return false
      }
      if (!this.addStaffInfo.sex) {
        this.$message.error(this.$t('addStaff.staffGenderPlaceholder'))
        return false
      }
      if (!this.addStaffInfo.tel || !/^1[3-9]\d{9}$/.test(this.addStaffInfo.tel)) {
        this.$message.error(this.$t('addStaff.phonePlaceholder'))
        return false
      }
      if (!this.addStaffInfo.address) {
        this.$message.error(this.$t('addStaff.addressPlaceholder'))
        return false
      }
      if (!this.addStaffInfo.orgId) {
        this.$message.error(this.$t('addStaff.relatedOrgPlaceholder'))
        return false
      }
      return true
    },
    async saveStaffInfo() {
      if (!this.validateForm()) return

      try {
        const data = {
          ...this.addStaffInfo,
          name: this.addStaffInfo.username
        }
        const res = await addStaff(data)
        if (res.code === 0) {
          this.$message.success('添加成功')
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg || '添加失败')
        }
      } catch (error) {
        this.$message.error(error.message || '添加失败')
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.add-staff-container {
  padding: 20px;
  text-align: left;

  .footer-buttons {
    margin-top: 20px;
    text-align: right;
  }

  .el-form-item {
    margin-bottom: 22px;
  }
}
</style>