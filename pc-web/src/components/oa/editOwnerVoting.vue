<template>
  <div class="edit-owner-voting-container">
    <el-card>
      <div slot="header">
        <span>{{ $t('ownerVoting.edit.title') }}</span>
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
            disabled
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
            disabled
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
            disabled
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
              disabled
            />
          </div>
        </el-form-item>

        <el-form-item>
          <el-button @click="cancel">
            {{ $t('common.back') }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getOwnerVoteDetail } from '@/api/oa/ownerVotingApi'

export default {
  name: 'EditOwnerVoting',
  data() {
    return {
      form: {
        qaId: '',
        qaName: '',
        titleType: '',
        startTime: '',
        endTime: '',
        titleValues: []
      },
      rules: {
        qaName: [
          { required: true, message: this.$t('ownerVoting.validate.qaName'), trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    async getDetail() {
      try {
        const qaId = this.$route.query.qaId
        if (!qaId) {
          this.$message.error(this.$t('ownerVoting.edit.noId'))
          this.$router.push('/views/oa/ownerVotingList')
          return
        }

        const data = await getOwnerVoteDetail({ qaId })
        this.form = {
          ...data,
          titleValues: data.titleValues.map((item, index) => ({
            ...item,
            seq: index + 1
          }))
        }
      } catch (error) {
        this.$message.error(error.message || this.$t('ownerVoting.edit.fetchError'))
        this.$router.push('/views/oa/ownerVotingList')
      }
    },
    cancel() {
      this.$router.push('/views/oa/ownerVotingList')
    }
  }
}
</script>

<style lang="scss" scoped>
.edit-owner-voting-container {
  padding: 20px;

  .option-item {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
}
</style>