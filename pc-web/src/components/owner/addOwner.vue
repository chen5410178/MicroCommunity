<template>
  <el-dialog :title="$t('listOwner.buttons.add')" :visible.sync="visible" width="800px" @close="resetForm">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwner.columns.type')" prop="personType">
            <el-select v-model="form.personType" style="width:100%">
              <el-option v-for="(type, index) in personTypes" :key="index" :label="type.label" :value="type.value" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('listOwner.columns.role')" prop="personRole">
            <el-select v-model="form.personRole" style="width:100%">
              <el-option v-for="role in personRoles" :key="role.value" :label="role.label" :value="role.value" />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('listOwner.columns.name')" prop="name">
            <el-input v-model="form.name" :placeholder="$t('listOwner.placeholders.name')" />
          </el-form-item>

          <el-form-item v-if="form.personType === 'C'" :label="$t('listOwner.buttons.member')">
            <el-input v-model="form.concactPerson" />
          </el-form-item>

          <el-form-item :label="$t('listOwner.columns.phone')" prop="link">
            <el-input v-model="form.link" :placeholder="$t('listOwner.placeholders.phone')" />
          </el-form-item>

          <el-form-item v-if="form.personType === 'P'" :label="$t('listOwner.columns.gender')" prop="sex">
            <el-select v-model="form.sex" style="width:100%">
              <el-option v-for="gender in genders" :key="gender.value" :label="gender.label" :value="gender.value" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12" class="text-center">
          <el-image style="width: 200px; height: 150px; border: 1px dashed #ccc;"
            :src="form.ownerPhotoUrl || '/img/noPhoto.jpg'" fit="cover" />
          <el-upload class="mt-10" :show-file-list="false" :before-upload="beforeUpload" :http-request="uploadImage">
            <el-button size="small" type="primary">
              {{ $t('common.upload') }}
            </el-button>
          </el-upload>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('listOwner.columns.backupPhone')">
            <el-input v-model="form.concactLink" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('listOwner.columns.address')">
            <el-input v-model="form.address" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item v-if="form.personType === 'C'" :label="$t('listOwner.columns.idCard')">
            <el-input v-model="form.idCard" />
          </el-form-item>

          <el-form-item v-else :label="$t('listOwner.columns.idCard')">
            <el-input v-model="form.idCard" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item :label="$t('common.remark')">
            <el-input v-model="form.remark" />
          </el-form-item>
        </el-col>
      </el-row>

      <div v-for="(item, index) in attrs" :key="index">
        <el-row :gutter="20" v-if="index % 2 === 0">
          <el-col :span="12">
            <el-form-item :label="item.specName">
              <el-input v-if="item.specType === '2233'" v-model="item.value" :placeholder="item.specHoldplace" />
              <el-select v-else-if="item.specType === '3344'" v-model="item.value" style="width:100%">
                <el-option v-for="val in item.values" :key="val.value" :label="val.valueName" :value="val.value" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="index < attrs.length - 1">
            <el-form-item :label="attrs[index + 1].specName">
              <el-input v-if="attrs[index + 1].specType === '2233'" v-model="attrs[index + 1].value"
                :placeholder="attrs[index + 1].specHoldplace" />
              <el-select v-else-if="attrs[index + 1].specType === '3344'" v-model="attrs[index + 1].value"
                style="width:100%">
                <el-option v-for="val in attrs[index + 1].values" :key="val.value" :label="val.valueName"
                  :value="val.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('listOwner.buttons.cancel') }}
      </el-button>
      <el-button type="primary" @click="saveOwner">
        {{ $t('listOwner.buttons.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOwner, getAttrValue, uploadImage } from '@/api/owner/ownerApi'
import { getAttrSpecList } from '@/api/dev/attrSpecApi'

export default {
  name: 'AddOwner',
  data() {
    return {
      visible: false,
      form: {
        name: '',
        link: '',
        address: '',
        sex: '0',
        remark: '',
        ownerPhoto: '',
        ownerPhotoUrl: '',
        idCard: '',
        personType: 'P',
        personRole: '1',
        concactPerson: '',
        concactLink: ''
      },
      attrs: [],
      personTypes: [
        { value: 'P', label: this.$t('listOwner.personType.personal') },
        { value: 'C', label: this.$t('listOwner.personType.company') }
      ],
      personRoles: [
        { value: '1', label: this.$t('listOwner.role.owner') },
        { value: '2', label: this.$t('listOwner.role.tenant') }
      ],
      genders: [
        { value: '0', label: this.$t('listOwner.gender.male') },
        { value: '1', label: this.$t('listOwner.gender.female') }
      ],
      rules: {
        name: [
          { required: true, message: this.$t('listOwner.placeholders.name'), trigger: 'blur' },
        ],
        personType: [
          { required: true, message: this.$t('listOwner.rules.typeRequired'), trigger: 'change' }
        ],
        personRole: [
          { required: true, message: this.$t('listOwner.rules.roleRequired'), trigger: 'change' }
        ],
        link: [
          { required: true, message: this.$t('listOwner.placeholders.name'), trigger: 'blur' },
        ],
        sex: [
          { required: true, message: this.$t('listOwner.rules.genderRequired'), trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.loadAttributes()
    },

    async loadAttributes() {
      try {
        const { data } = await getAttrSpecList({ page: 1, row: 100, tableName: 'building_owner_attr' })
        this.attrs = data.filter(item => item.specShow === 'Y')

        for (const attr of this.attrs) {
          attr.value = ''
          if (attr.specType === '3344') {
            attr.values = await getAttrValue(attr.specCd)
            attr.value = ''
          }
        }
      } catch (error) {
        console.error('Failed to load attributes:', error)
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
        this.form.ownerPhoto = res.fileId
        this.form.ownerPhotoUrl = res.url
      } catch (error) {
        this.$message.error(this.$t('listOwner.upload.failed'))
      }
    },

    async saveOwner() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          const params = {
            ...this.form,
            attrs: this.attrs.map(attr => ({
              specCd: attr.specCd,
              value: attr.value
            }))
          }

          await saveOwner(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          this.$message.error(error || this.$t('common.submitFailed'))
        }
      })
    },

    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        name: '',
        link: '',
        address: '',
        sex: '0',
        remark: '',
        ownerPhoto: '',
        ownerPhotoUrl: '',
        idCard: '',
        personType: 'P',
        personRole: '1',
        concactPerson: '',
        concactLink: ''
      }
      this.attrs.forEach(attr => {
        attr.value = ''
      })
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}

.mt-10 {
  margin-top: 10px;
}
</style>