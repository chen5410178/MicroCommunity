<template>
  <el-dialog :title="$t('editOwnerMember.title')" :visible.sync="dialogVisible" width="60%"
    @close="_closeEditOwnerMemberModal">
    <el-form :model="editOwnerMemberInfo" :rules="rules" ref="form" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editOwnerMember.personRole')" prop="personRole">
            <el-select v-model="editOwnerMemberInfo.personRole"
              :placeholder="$t('editOwnerMember.placeholder.personRole')" class="w100">
              <el-option v-for="item in roleOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('editOwnerMember.name')" prop="name">
            <el-input v-model.trim="editOwnerMemberInfo.name" :placeholder="$t('editOwnerMember.placeholder.name')"
              clearable />
          </el-form-item>

          <el-form-item :label="$t('editOwnerMember.link')" prop="link">
            <el-input v-model.trim="editOwnerMemberInfo.link" :placeholder="$t('editOwnerMember.placeholder.link')"
              clearable maxlength="11" />
          </el-form-item>

          <el-form-item :label="$t('editOwnerMember.sex')" prop="sex" v-if="editOwnerMemberInfo.personType === 'P'">
            <el-select v-model="editOwnerMemberInfo.sex" :placeholder="$t('editOwnerMember.placeholder.sex')"
              class="w100">
              <el-option v-for="item in sexOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('editOwnerMember.address')">
            <el-input v-model.trim="editOwnerMemberInfo.address"
              :placeholder="$t('editOwnerMember.placeholder.address')" clearable />
          </el-form-item>
        </el-col>

        <el-col :span="12" class="text-center">
          <div class="avatar-uploader">
            <img v-if="editOwnerMemberInfo.ownerPhotoUrl" :src="editOwnerMemberInfo.ownerPhotoUrl" class="avatar"
              :alt="$t('editOwnerMember.photo')" @error="handleImageError">
            <img v-else src="/img/noPhoto.jpg" class="avatar" :alt="$t('editOwnerMember.photo')">
          </div>
          <el-button type="primary" class="mt10" @click="_uploadEditMemberPhoto">
            {{ $t('editOwnerMember.uploadPhoto') }}
          </el-button>
          <input type="file" ref="fileInput" accept=".jpg,.jpeg,.png" @change="_chooseEditMemberPhoto"
            style="display: none">
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editOwnerMember.concactLink')">
            <el-input v-model.trim="editOwnerMemberInfo.concactLink"
              :placeholder="$t('editOwnerMember.placeholder.concactLink')" clearable />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('editOwnerMember.idCard')">
            <el-input v-model.trim="editOwnerMemberInfo.idCard" :placeholder="$t('editOwnerMember.placeholder.idCard')"
              clearable />
          </el-form-item>

          <el-form-item :label="$t('editOwnerMember.remark')">
            <el-input v-model.trim="editOwnerMemberInfo.remark" :placeholder="$t('editOwnerMember.placeholder.remark')"
              clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <template v-for="(item, index) in editOwnerMemberInfo.attrs">
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

          <el-col :span="12" v-if="index < editOwnerMemberInfo.attrs.length - 1">
            <el-form-item :label="editOwnerMemberInfo.attrs[index + 1].specName"
              v-if="editOwnerMemberInfo.attrs[index + 1].specType === '2233'">
              <el-input v-model="editOwnerMemberInfo.attrs[index + 1].value"
                :placeholder="editOwnerMemberInfo.attrs[index + 1].specHoldplace" clearable />
            </el-form-item>

            <el-form-item :label="editOwnerMemberInfo.attrs[index + 1].specName"
              v-if="editOwnerMemberInfo.attrs[index + 1].specType === '3344'">
              <el-select v-model="editOwnerMemberInfo.attrs[index + 1].value"
                :placeholder="editOwnerMemberInfo.attrs[index + 1].specHoldplace" class="w100">
                <el-option v-for="value in editOwnerMemberInfo.attrs[index + 1].values" :key="value.value"
                  :label="value.valueName" :value="value.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="_closeEditOwnerMemberModal">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editOwnerMemberMethod">{{ $t('common.save') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'
import { getAttrValueList } from '@/api/dev/attrValueApi'
import { editOwnerMember } from '@/api/owner/addOwnerMemberApi'

import { uploadFile } from '@/api/staff/addStaffApi'

export default {
  name: 'EditOwnerMember',
  data() {
    return {
      dialogVisible: false,
      editOwnerMemberInfo: {
        ownerId: '',
        memberId: '',
        ownerTypeCd: '',
        name: '',
        link: '',
        address: '',
        sex: '',
        remark: '',
        ownerPhoto: '',
        ownerPhotoUrl: '',
        idCard: '',
        personType: 'P',
        personRole: '1',
        concactLink: '',
        attrs: []
      },
      roleOptions: [
        { value: '2', label: this.$t('editOwnerMember.role.tenant') },
        { value: '3', label: this.$t('editOwnerMember.role.family') },
        { value: '4', label: this.$t('editOwnerMember.role.staff') },
        { value: '99', label: this.$t('editOwnerMember.role.other') }
      ],
      sexOptions: [
        { value: '0', label: this.$t('common.male') },
        { value: '1', label: this.$t('common.female') }
      ],
      rules: {
        personRole: [
          { required: true, message: this.$t('editOwnerMember.placeholder.personRole'), trigger: 'change' }
        ],
        name: [
          { required: true, message: this.$t('editOwnerMember.placeholder.name'), trigger: 'blur' }
        ],
        link: [
          { required: true, message: this.$t('editOwnerMember.placeholder.link'), trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: this.$t('editOwnerMember.validate.phoneFormat'), trigger: 'blur' }
        ],
        sex: [
          { required: true, message: this.$t('editOwnerMember.placeholder.sex'), trigger: 'change' }
        ],
        idCard: [
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: this.$t('editOwnerMember.validate.idCardFormat'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('editOwnerMember.validate.remarkLength'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(member) {
      this.editOwnerMemberInfo = {
        ...member,
        ownerPhoto: member.faceUrl,
        ownerPhotoUrl: member.faceUrl,
        attrs: []
      }

      this._loadEditOwnerMemberAttrSpec().then(() => {
        if (Object.prototype.hasOwnProperty.call(member, 'ownerAttrDtos')) {
          const ownerAttrDtos = member.ownerAttrDtos
          this.editOwnerMemberInfo.attrs.forEach(attrItem => {
            ownerAttrDtos.forEach(item => {
              if (item.specCd === attrItem.specCd) {
                attrItem.attrId = item.attrId
                attrItem.value = item.value
              }
            })
          })
        }

        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs.form.clearValidate()
        })
      })
    },

    editOwnerMemberMethod() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const params = {
            ...this.editOwnerMemberInfo,
            communityId: getCommunityId()
          }

          editOwnerMember(params)
            .then(res => {
              if (res.code === 0) {
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

    _loadEditOwnerMemberAttrSpec() {
      return new Promise(resolve => {
        this.editOwnerMemberInfo.attrs = []
        getAttrSpecList({ page: 1, row: 100, tableName: 'building_owner_attr' }).then(data => {
          data.data.forEach(item => {
            if (item.specShow === 'Y') {
              item.value = ''
              item.values = []
              this._loadEditMemberAttrValue(item.specCd, item.values)
              this.editOwnerMemberInfo.attrs.push(item)
            }
          })
          resolve()
        })
      })
    },

    _loadEditMemberAttrValue(specCd, values) {
      getAttrValueList({ specCd: specCd, page: 1, row: 100 }).then(data => {
        data.data.forEach(item => {
          if (item.valueShow === 'Y') {
            values.push(item)
          }
        })
      })
    },

    _uploadEditMemberPhoto() {
      this.$refs.fileInput.click()
    },

    _chooseEditMemberPhoto(event) {
      const files = event.target.files
      if (files && files.length > 0) {
        const file = files[0]
        if (file.size > 1024 * 1024 * 2) {
          this.$message.error(this.$t('editOwnerMember.validate.photoSize'))
          return
        }

        this._doUploadImageEditOwnerMember(file)
      }
    },

    _doUploadImageEditOwnerMember(file) {
      const formData = new FormData()
      formData.append('uploadFile', file)
      formData.append('communityId', getCommunityId())

      uploadFile(formData).then(res => {
        this.editOwnerMemberInfo.ownerPhoto = res.fileId
        this.editOwnerMemberInfo.ownerPhotoUrl = res.url
      }).catch(err => {
        console.error('上传失败:', err)
        this.$message.error(this.$t('editOwnerMember.uploadFailed'))
      })
    },

    _closeEditOwnerMemberModal() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },

    handleImageError(e) {
      e.target.src = '/img/noPhoto.jpg'
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