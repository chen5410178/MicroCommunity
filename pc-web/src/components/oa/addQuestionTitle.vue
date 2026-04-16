<template>
  <el-dialog :title="$t('questionTitle.add.title')" :visible.sync="visible" width="40%" @close="handleClose">
    <el-form ref="form" :model="formData" label-width="120px">
      <el-form-item :label="$t('questionTitle.add.name')" prop="qaTitle" :rules="[
        { required: true, message: $t('questionTitle.add.nameRequired'), trigger: 'blur' },
        { max: 256, message: $t('questionTitle.add.nameTooLong'), trigger: 'blur' }
      ]">
        <el-input v-model="formData.qaTitle" :placeholder="$t('questionTitle.add.namePlaceholder')" />
      </el-form-item>

      <el-form-item :label="$t('questionTitle.add.type')" prop="titleType" :rules="[
        { required: true, message: $t('questionTitle.add.typeRequired'), trigger: 'change' }
      ]">
        <el-select v-model="formData.titleType" :placeholder="$t('questionTitle.add.typePlaceholder')" style="width:100%"
          @change="handleTypeChange">
          <el-option :label="$t('questionTitle.add.pleaseSelect')" value="" disabled />
          <el-option :label="$t('questionTitle.types.singleChoice')" value="1001" />
          <el-option :label="$t('questionTitle.types.multipleChoice')" value="2002" />
          <el-option :label="$t('questionTitle.types.shortAnswer')" value="3003" />
        </el-select>
      </el-form-item>

      <template v-if="formData.titleType && formData.titleType !== '3003'">
        <el-form-item v-for="(item, index) in formData.titleValues" :key="index"
          :label="`${$t('questionTitle.add.option')}${item.seq}`" :prop="`titleValues.${index}.itemValue`" :rules="[
            { required: true, message: $t('questionTitle.add.optionRequired'), trigger: 'blur' }
          ]">
          <el-row :gutter="20">
            <el-col :span="18">
              <el-input v-model="item.itemValue" :placeholder="$t('questionTitle.add.optionPlaceholder')" />
            </el-col>
            <el-col :span="5">
              <el-button v-if="index === formData.titleValues.length - 1" type="text" @click="addOption">
                {{ $t('questionTitle.add.addOption') }}
              </el-button>
              <el-button v-else type="text" @click="removeOption(index)">
                {{ $t('questionTitle.add.removeOption') }}
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
import { saveQuestionTitle } from '@/api/oa/questionTitleApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddQuestionTitle',
  data() {
    return {
      visible: false,
      formData: {
        titleType: '',
        qaTitle: '',
        titleValues: [],
        communityId: ''
      }
    }
  },
  methods: {
    open() {
      this.resetForm()
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
        titleType: '',
        qaTitle: '',
        titleValues: [],
        communityId: getCommunityId()
      }
    },
    handleTypeChange(value) {
      this.formData.titleValues = []
      if (value === '1001') {
        this.formData.titleValues = [{ itemValue: '', seq: 1 }]
      } else if (value === '2002') {
        this.formData.titleValues = [
          { itemValue: '', seq: 1 },
          { itemValue: '', seq: 2 }
        ]
      }
    },
    addOption() {
      const newSeq = this.formData.titleValues.length + 1
      this.formData.titleValues.push({ itemValue: '', seq: newSeq })
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

        await saveQuestionTitle(this.formData)
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