<template>
  <el-dialog :title="$t('addOwnerMember.title')" :visible.sync="dialogVisible" width="60%"
    @close="_closeSaveOwnerModal">
    <el-form :model="addOwnerMemberInfo" :rules="rules" ref="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerMember.personRole')" prop="personRole">
            <el-select v-model="addOwnerMemberInfo.personRole"
              :placeholder="$t('addOwnerMember.placeholder.personRole')" class="w100">
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('addOwnerMember.name')" prop="name">
            <el-input v-model.trim="addOwnerMemberInfo.name" :placeholder="$t('addOwnerMember.placeholder.name')"
              clearable />
          </el-form-item>

          <el-form-item :label="$t('addOwnerMember.link')" prop="link">
            <el-input v-model.trim="addOwnerMemberInfo.link" :placeholder="$t('addOwnerMember.placeholder.link')"
              clearable />
          </el-form-item>

          <el-form-item :label="$t('addOwnerMember.sex')" prop="sex">
            <el-select v-model="addOwnerMemberInfo.sex" :placeholder="$t('addOwnerMember.placeholder.sex')"
              class="w100">
              <el-option v-for="item in sexOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12" class="text-center">
          <div class="avatar-uploader">
            <img v-if="addOwnerMemberInfo.ownerPhotoUrl" :src="addOwnerMemberInfo.ownerPhotoUrl" class="avatar"
              :alt="$t('addOwnerMember.photo')">
            <img v-else src="/img/noPhoto.jpg" class="avatar" :alt="$t('addOwnerMember.photo')">
          </div>
          <el-button type="primary" class="mt10" @click="_uploadPhoto">
            {{ $t('addOwnerMember.uploadPhoto') }}
          </el-button>
          <input type="file" ref="fileInput" accept=".jpg,.jpeg,.png" @change="_choosePhoto" style="display: none">
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addOwnerMember.concactLink')">
            <el-input v-model.trim="addOwnerMemberInfo.concactLink"
              :placeholder="$t('addOwnerMember.placeholder.concactLink')" clearable />
          </el-form-item>

          <el-form-item :label="$t('addOwnerMember.address')">
            <el-input v-model.trim="addOwnerMemberInfo.address" :placeholder="$t('addOwnerMember.placeholder.address')"
              clearable />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('addOwnerMember.idCard')">
            <el-input v-model.trim="addOwnerMemberInfo.idCard" :placeholder="$t('addOwnerMember.placeholder.idCard')"
              clearable />
          </el-form-item>

          <el-form-item :label="$t('addOwnerMember.remark')">
            <el-input v-model.trim="addOwnerMemberInfo.remark" :placeholder="$t('addOwnerMember.placeholder.remark')"
              clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <template v-for="(item, index) in addOwnerMemberInfo.attrs">
        <el-row :gutter="20" v-if="index % 2 === 0" :key="index">
          <el-col :span="12">
            <el-form-item :label="item.specName" v-if="item.specType === '2233'">
              <el-input v-model="item.value" :placeholder="item.specHoldplace" clearable />
            </el-form-item>

            <el-form-item :label="item.specName" v-if="item.specType === '3344'">
              <el-select v-model="item.value" :placeholder="item.specHoldplace" class="w100">
                <el-option v-for="value in item.values" :key="value.value" :label="value.valueName"
                  :value="value.value" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="index < addOwnerMemberInfo.attrs.length - 1">
            <el-form-item :label="addOwnerMemberInfo.attrs[index + 1].specName"
              v-if="addOwnerMemberInfo.attrs[index + 1].specType === '2233'">
              <el-input v-model="addOwnerMemberInfo.attrs[index + 1].value"
                :placeholder="addOwnerMemberInfo.attrs[index + 1].specHoldplace" clearable />
            </el-form-item>

            <el-form-item :label="addOwnerMemberInfo.attrs[index + 1].specName"
              v-if="addOwnerMemberInfo.attrs[index + 1].specType === '3344'">
              <el-select v-model="addOwnerMemberInfo.attrs[index + 1].value"
                :placeholder="addOwnerMemberInfo.attrs[index + 1].specHoldplace" class="w100">
                <el-option v-for="value in addOwnerMemberInfo.attrs[index + 1].values" :key="value.value"
                  :label="value.valueName" :value="value.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="_closeSaveOwnerModal">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveOwnerMemberInfo">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getAttrValue } from '@/api/owner/ownerApi'
import { uploadFile } from '@/api/staff/addStaffApi'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import { saveOwnerMember } from '@/api/owner/addOwnerMemberApi'


export default {
  name: 'AddOwnerMember',
  data() {
    return {
      dialogVisible: false,
      addOwnerMemberInfo: {
        name: '',
        link: '',
        address: '',
        sex: '',
        remark: '',
        ownerId: '',
        ownerPhoto: '',
        ownerPhotoUrl: '',
        idCard: '',
        personType: 'P',
        personRole: '3',
        concactLink: '',
        attrs: []
      },
      roleOptions: [
        { value: '2', label: this.$t('addOwnerMember.role.tenant') },
        { value: '3', label: this.$t('addOwnerMember.role.family') },
        { value: '4', label: this.$t('addOwnerMember.role.staff') },
        { value: '99', label: this.$t('addOwnerMember.role.other') }
      ],
      sexOptions: [
        { value: '0', label: this.$t('common.male') },
        { value: '1', label: this.$t('common.female') }
      ],
      rules: {
        personRole: [
          { required: true, message: this.$t('addOwnerMember.placeholder.personRole'), trigger: 'change' }
        ],
        name: [
          { required: true, message: this.$t('addOwnerMember.placeholder.name'), trigger: 'blur' },
          { min: 2, max: 64, message: this.$t('addOwnerMember.validate.nameLength'), trigger: 'blur' }
        ],
        link: [
          { required: true, message: this.$t('addOwnerMember.placeholder.link'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('addOwnerMember.validate.phoneFormat'), trigger: 'blur' }
        ],
        sex: [
          { required: true, message: this.$t('addOwnerMember.placeholder.sex'), trigger: 'change' }
        ],
        idCard: [
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: this.$t('addOwnerMember.validate.idCardFormat'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('addOwnerMember.validate.remarkLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(params) {
      this.addOwnerMemberInfo.ownerId = params.ownerId
      this._loadOwnerAttrSpec()
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },

    async saveOwnerMemberInfo() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const params = {
            ...this.addOwnerMemberInfo,
            communityId: getCommunityId()
          }
          saveOwnerMember(params)
            .then(res => {
              if (res.code == 0) {
                this.$message.success(this.$t('common.operationSuccess'))
                this.dialogVisible = false
                this.$emit('success')
              } else {
                this.$message.error(res.msg || this.$t('common.saveFailed'))
              }
            })
            .catch(err => {
              console.error('请求失败:', err)
              this.$message.error(this.$t('common.saveFailed'))
            })
        }
      })
    },

    async _loadOwnerAttrSpec() {
      this.addOwnerMemberInfo.attrs = []
      const { data } = await getAttrSpecList({ page: 1, row: 100, tableName: 'building_owner_attr' })
      for (const attr of data) {
         attr.value = ''
        if (attr.specShow == 'Y') {
          attr.values = await getAttrValue(attr.specCd)
          this.addOwnerMemberInfo.attrs.push(attr)
        }

      }
    },

    _uploadPhoto() {
      this.$refs.fileInput.click()
    },

    _choosePhoto(event) {
      const files = event.target.files
      if (files && files.length > 0) {
        const file = files[0]
        if (file.size > 1024 * 1024 * 2) {
          this.$message.error(this.$t('addOwnerMember.validate.photoSize'))
          return
        }
        this._doUploadImage(file)
      }
    },

    _doUploadImage(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      formData.append('communityId', getCommunityId())

      uploadFile(formData).then(res => {
        this.addOwnerMemberInfo.ownerPhoto = res.fileId
        this.addOwnerMemberInfo.ownerPhotoUrl = res.url
      }).catch(err => {
        console.error('上传失败:', err)
        this.$message.error(this.$t('addOwnerMember.uploadFailed'))
      })
    },

    _closeSaveOwnerModal() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
      this.addOwnerMemberInfo = {
        name: '',
        link: '',
        address: '',
        sex: '',
        remark: '',
        ownerId: '',
        ownerPhoto: '',
        ownerPhotoUrl: '',
        idCard: '',
        personType: 'P',
        personRole: '3',
        concactLink: '',
        attrs: []
      }
    }
  }
}
</script>

<style scoped>
.w100 {
  width: 100%;
}

.mt10 {
  margin-top: 10px;
}

.text-center {
  text-align: center;
}

.avatar-uploader {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.avatar {
  width: 150px;
  height: 150px;
  object-fit: cover;
}
</style>