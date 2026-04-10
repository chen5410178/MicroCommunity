<template>
  <div class="add-owner-voting-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>{{ $t('addOwnerVoting.title') }}</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="addOwnerVotingInfo" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerVoting.voteName')" prop="qaName">
                  <el-input v-model="addOwnerVotingInfo.qaName" :placeholder="$t('addOwnerVoting.voteNamePlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerVoting.type')" prop="titleType">
                  <el-select v-model="addOwnerVotingInfo.titleType" :placeholder="$t('addOwnerVoting.typePlaceholder')"
                    style="width:100%" @change="changeAddTitleType">
                    <el-option :label="$t('addOwnerVoting.singleChoice')" value="1001" />
                    <el-option :label="$t('addOwnerVoting.multipleChoice')" value="2002" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerVoting.startTime')" prop="startTime">
                  <el-date-picker v-model="addOwnerVotingInfo.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                    :placeholder="$t('addOwnerVoting.startTimePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerVoting.endTime')" prop="endTime">
                  <el-date-picker v-model="addOwnerVotingInfo.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                    :placeholder="$t('addOwnerVoting.endTimePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row v-for="(item, index) in addOwnerVotingInfo.titleValues" :key="index" :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('addOwnerVoting.option') + item.seq">
                  <el-input v-model="item.itemValue" :placeholder="$t('addOwnerVoting.optionPlaceholder')" />
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-button v-if="index === addOwnerVotingInfo.titleValues.length - 1" type="text" @click="addTitleValue">
                  {{ $t('addOwnerVoting.addOption') }}
                </el-button>
                <el-button v-else type="text" @click="deleteTitleValue(item.seq)">
                  {{ $t('addOwnerVoting.deleteOption') }}
                </el-button>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item :label="$t('addOwnerVoting.voteDescription')">
                  <el-input v-model="addOwnerVotingInfo.content" type="textarea" :rows="4"
                    :placeholder="$t('addOwnerVoting.voteDescriptionPlaceholder')" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24">
                <el-form>
                  <el-form-item :label="$t('addOwnerVoting.voteRooms')">
                    <SelectRooms ref="selectRooms" @notifySelectRooms="handleSelectRooms" />
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="24" class="text-right">
                <el-button type="warning" @click="goBack">
                  {{ $t('common.cancel') }}
                </el-button>
                <el-button type="primary" @click="saveOwnerVoting">
                  <i class="el-icon-check" /> {{ $t('common.save') }}
                </el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import SelectRooms from '@/components/room/selectRooms'
import { saveOwnerVote } from '@/api/oa/addOwnerVotingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddOwnerVoting',
  components: {
    SelectRooms
  },
  data() {
    return {
      addOwnerVotingInfo: {
        qaName: '',
        startTime: '',
        endTime: '',
        communityId: '',
        content: '',
        titleType: '',
        titleValues: [],
        roomIds: []
      }
    }
  },
  created() {
    this.addOwnerVotingInfo.communityId = getCommunityId()
  },
  methods: {
    handleSelectRooms(rooms) {
      this.addOwnerVotingInfo.roomIds = rooms.map(item => item.roomId)
    },
    validateForm() {
      if (!this.addOwnerVotingInfo.qaName) {
        this.$message.error(this.$t('addOwnerVoting.validate.voteName'))
        return false
      }
      if (!this.addOwnerVotingInfo.titleType) {
        this.$message.error(this.$t('addOwnerVoting.validate.type'))
        return false
      }
      if (!this.addOwnerVotingInfo.startTime) {
        this.$message.error(this.$t('addOwnerVoting.validate.startTime'))
        return false
      }
      if (!this.addOwnerVotingInfo.endTime) {
        this.$message.error(this.$t('addOwnerVoting.validate.endTime'))
        return false
      }
      if (this.addOwnerVotingInfo.titleValues.some(item => !item.itemValue)) {
        this.$message.error(this.$t('addOwnerVoting.validate.option'))
        return false
      }
      return true
    },
    async saveOwnerVoting() {
      if (!this.validateForm()) return

      try {
        const res = await saveOwnerVote(this.addOwnerVotingInfo)
        if (res.code === 0) {
          this.$message.success(this.$t('common.operationSuccess'))
          this.$router.go(-1)
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error(this.$t('common.saveError'))
      }
    },
    changeAddTitleType() {
      const type = this.addOwnerVotingInfo.titleType
      if (type === '1001') {
        this.addOwnerVotingInfo.titleValues = [{
          itemValue: '',
          seq: 1
        }]
      } else if (type === '2002') {
        this.addOwnerVotingInfo.titleValues = [
          {
            itemValue: '',
            seq: 1
          },
          {
            itemValue: '',
            seq: 2
          }
        ]
      } else {
        this.addOwnerVotingInfo.titleValues = []
      }
    },
    addTitleValue() {
      this.addOwnerVotingInfo.titleValues.push({
        itemValue: '',
        seq: this.addOwnerVotingInfo.titleValues.length + 1
      })
    },
    deleteTitleValue(seq) {
      this.addOwnerVotingInfo.titleValues = this.addOwnerVotingInfo.titleValues
        .filter(item => item.seq !== seq)
        .map((item, index) => ({
          ...item,
          seq: index + 1
        }))
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.add-owner-voting-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .text-right {
    text-align: right;
  }
}
</style>