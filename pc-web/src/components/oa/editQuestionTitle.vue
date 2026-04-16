<template>
  <el-dialog :title="$t('questionTitle.edit.title')" :visible.sync="visible" width="70%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('questionTitle.edit.type')" prop="titleType" :rules="[
        { required: true, message: $t('questionTitle.edit.typeRequired'), trigger: 'change' }
      ]">
        <el-select v-model="formData.titleType" :placeholder="$t('questionTitle.edit.typePlaceholder')" style="width:100%"
          @change="handleTypeChange">
          <el-option :label="$t('questionTitle.edit.pleaseSelect')" value="" disabled />
          <el-option :label="$t('questionTitle.types.singleChoice')" value="1001" />
          <el-option :label="$t('questionTitle.types.multipleChoice')" value="2002" />
          <el-option :label="$t('questionTitle.types.shortAnswer')" value="3003" />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('questionTitle.edit.name')" prop="qaTitle" :rules="[
        { required: true, message: $t('questionTitle.edit.nameRequired'), trigger: 'blur' },
        { max: 256, message: $t('questionTitle.edit.nameTooLong'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.qaTitle" :placeholder="$t('questionTitle.edit.namePlaceholder')" />
      </el-form-item>

      <template v-if="formData.titleType && formData.titleType !== '3003'">
        <el-form-item v-for="(item, index) in formData.titleValues" :key="index"
          :label="`${$t('questionTitle.edit.option')}${item.seq}`" :prop="`titleValues.${index}.qaValue`" :rules="[
            { required: true, message: $t('questionTitle.edit.optionRequired'), trigger: 'blur' }
          ]">
          <el-row :gutter="20">
            <el-col :span="18">
              <el-input v-model="item.qaValue" :placeholder="$t('questionTitle.edit.optionPlaceholder')" />
            </el-col>
            <el-col :span="5">
              <el-button v-if="index === formData.titleValues.length - 1" type="text" @click="addOption">
                {{ $t('questionTitle.edit.addOption') }}
              </el-button>
              <el-button v-else type="text" @click="removeOption(index)">
                {{ $t('questionTitle.edit.removeOption') }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </template>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateQuestionTitle } from '@/api/oa/questionTitleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditQuestionTitle',
  data() {
    return {
      visible: false,
      formData: {
        titleId: '',
        titleType: '',
        qaTitle: '',
        titleValues: [],
        communityId: ''
      }
    }
  },
  methods: {
    open(data) {
      this.resetForm()
      this.formData = {
        ...data,
        communityId: getCommunityId()
      }
      this.visible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    resetForm() {
      this.formData = {
        titleId: '',
        titleType: '',
        qaTitle: '',
        titleValues: [],
        communityId: getCommunityId()
      }
    },
    handleTypeChange(value) {
      if (value === '3003') {
        this.formData.titleValues = []
      } else if (value === '1001' && this.formData.titleValues.length === 0) {
        this.formData.titleValues = [{ qaValue: '', seq: 1 }]
      } else if (value === '2002' && this.formData.titleValues.length < 2) {
        this.formData.titleValues = [
          { qaValue: '', seq: 1 },
          { qaValue: '', seq: 2 }
        ]
      }
    },
    addOption() {
      const newSeq = this.formData.titleValues.length + 1
      this.formData.titleValues.push({ qaValue: '', seq: newSeq })
    },
    removeOption(index) {
      this.formData.titleValues.splice(index, 1)
      // 重新排序
      this.formData.titleValues.forEach((item, i) => {
        item.seq = i + 1
      })
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.form.validate()
        if (!valid) return

        // 如果是简答题，清空选项
        if (this.formData.titleType === '3003') {
          this.formData.titleValues = []
        }

        await updateQuestionTitle(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>