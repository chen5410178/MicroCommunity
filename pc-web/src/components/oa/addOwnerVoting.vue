<template>
  <div class="add-owner-voting-container">
    <el-card>
      <div slot="header">
        <span>{{ $t('ownerVoting.add.title') }}</span>
      </div>
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        label-position="right"
      >
        <el-form-item
          :label="$t('ownerVoting.form.qaName')"
          prop="qaName"
        >
          <el-input
            v-model="form.qaName"
            :placeholder="$t('ownerVoting.form.qaNamePlaceholder')"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item
          :label="$t('ownerVoting.form.type')"
          prop="titleType"
        >
          <el-select
            v-model="form.titleType"
            :placeholder="$t('ownerVoting.form.typePlaceholder')"
            style="width: 100%"
          >
            <el-option
              :label="$t('ownerVoting.type.single')"
              value="1001"
            />
            <el-option
              :label="$t('ownerVoting.type.multiple')"
              value="2002"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          :label="$t('ownerVoting.form.startTime')"
          prop="startTime"
        >
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            :placeholder="$t('ownerVoting.form.startTimePlaceholder')"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item
          :label="$t('ownerVoting.form.endTime')"
          prop="endTime"
        >
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            :placeholder="$t('ownerVoting.form.endTimePlaceholder')"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item
          :label="$t('ownerVoting.form.options')"
          prop="titleValues"
        >
          <div
            v-for="(item, index) in form.titleValues"
            :key="index"
            class="option-item"
          >
            <el-input
              v-model="item.qaValue"
              :placeholder="$t('ownerVoting.form.optionPlaceholder', { num: index + 1 })"
              style="width: 80%; margin-right: 10px"
            />
            <el-button
              v-if="index > 0"
              type="danger"
              icon="el-icon-minus"
              circle
              @click="removeOption(index)"
            />
          </div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            @click="addOption"
          >
            {{ $t('ownerVoting.form.addOption') }}
          </el-button>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="submitForm"
          >
            {{ $t('common.submit') }}
          </el-button>
          <el-button @click="cancel">
            {{ $t('common.cancel') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { addOwnerVote } from '@/api/oa/ownerVotingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddOwnerVoting',
  data() {
    return {
      form: {
        qaName: '',
        titleType: '1001',
        startTime: '',
        endTime: '',
        titleValues: [
          { qaValue: '', seq: 1 }
        ],
        communityId: getCommunityId()
      },
      rules: {
        qaName: [
          { required: true, message: this.$t('ownerVoting.validate.qaName'), trigger: 'blur' }
        ],
        titleType: [
          { required: true, message: this.$t('ownerVoting.validate.type'), trigger: 'change' }
        ],
        startTime: [
          { required: true, message: this.$t('ownerVoting.validate.startTime'), trigger: 'change' }
        ],
        endTime: [
          { required: true, message: this.$t('ownerVoting.validate.endTime'), trigger: 'change' },
          { validator: this.validateEndTime, trigger: 'change' }
        ],
        titleValues: [
          { validator: this.validateOptions, trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    validateEndTime(rule, value, callback) {
      if (!value) {
        callback(new Error(this.$t('ownerVoting.validate.endTime')))
        return
      }
      if (new Date(value) <= new Date(this.form.startTime)) {
        callback(new Error(this.$t('ownerVoting.validate.endTimeAfterStart')))
      } else {
        callback()
      }
    },
    validateOptions(rule, value, callback) {
      if (value.length < 1) {
        callback(new Error(this.$t('ownerVoting.validate.atLeastOneOption')))
        return
      }
      for (let i = 0; i < value.length; i++) {
        if (!value[i].qaValue || value[i].qaValue.trim() === '') {
          callback(new Error(this.$t('ownerVoting.validate.optionNotEmpty')))
          return
        }
      }
      callback()
    },
    addOption() {
      this.form.titleValues.push({
        qaValue: '',
        seq: this.form.titleValues.length + 1
      })
    },
    removeOption(index) {
      this.form.titleValues.splice(index, 1)
      // 重新排序
      this.form.titleValues.forEach((item, i) => {
        item.seq = i + 1
      })
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            await addOwnerVote(this.form)
            this.$message.success(this.$t('common.operationSuccess'))
            this.$router.push('/views/oa/ownerVotingList')
          } catch (error) {
            this.$message.error(error.message || this.$t('ownerVoting.add.error'))
          }
        }
      })
    },
    cancel() {
      this.$router.push('/views/oa/ownerVotingList')
    }
  }
}
</script>

<style lang="scss" scoped>
.add-owner-voting-container {
  padding: 20px;

  .option-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
}
</style>