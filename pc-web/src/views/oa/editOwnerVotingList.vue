<template>
  <div class="edit-owner-voting-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>{{ $t('editOwnerVoting.title') }}</span>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form ref="form" :model="editOwnerVotingInfo" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editOwnerVoting.voteName')" prop="qaName">
                  <el-input v-model="editOwnerVotingInfo.qaName" :placeholder="$t('editOwnerVoting.voteNamePlaceholder')"
                    clearable />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editOwnerVoting.type')" prop="titleType">
                  <el-select v-model="editOwnerVotingInfo.titleType" :placeholder="$t('editOwnerVoting.typePlaceholder')"
                    @change="changeAddTitleType" style="width:100%">
                    <el-option :label="$t('editOwnerVoting.singleChoice')" value="1001" />
                    <el-option :label="$t('editOwnerVoting.multipleChoice')" value="2002" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item :label="$t('editOwnerVoting.startTime')" prop="startTime">
                  <el-date-picker v-model="editOwnerVotingInfo.startTime" type="datetime"  value-format="yyyy-MM-dd HH:mm:ss"
                    :placeholder="$t('editOwnerVoting.startTimePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('editOwnerVoting.endTime')" prop="endTime">
                  <el-date-picker v-model="editOwnerVotingInfo.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
                    :placeholder="$t('editOwnerVoting.endTimePlaceholder')" style="width:100%" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item v-for="(item, index) in editOwnerVotingInfo.titleValues" :key="index"
              :label="`${$t('editOwnerVoting.option')}${item.seq}`">
              <el-row :gutter="20">
                <el-col :span="18">
                  <el-input v-model="item.qaValue" :placeholder="$t('editOwnerVoting.optionPlaceholder')" />
                </el-col>
                <el-col :span="5">
                  <el-button v-if="index === editOwnerVotingInfo.titleValues.length - 1" type="text" @click="addTitleValue">
                    {{ $t('editOwnerVoting.addOption') }}
                  </el-button>
                  <el-button v-else type="text" @click="deleteTitleValue(item.seq)">
                    {{ $t('editOwnerVoting.deleteOption') }}
                  </el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item :label="$t('editOwnerVoting.description')">
              <el-input type="textarea" :rows="5" v-model="editOwnerVotingInfo.content"
                :placeholder="$t('editOwnerVoting.descriptionPlaceholder')" />
            </el-form-item>

            <el-form-item :label="$t('editOwnerVoting.voteRooms')">
              <div v-show="editOwnerVotingInfo.updateRoomIds">
                <select-rooms ref="selectRooms" @notifySelectRooms="handleSelectRooms" />
              </div>
              <div v-if="!editOwnerVotingInfo.updateRoomIds">
                <span>{{ editOwnerVotingInfo.voteCount }}{{ $t('editOwnerVoting.houseVote') }}</span>
                <el-button type="text" @click="updateRoomIdsMethod">
                  {{ $t('editOwnerVoting.reselect') }}
                </el-button>
              </div>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveOwnerVoting">
                <i class="el-icon-check"></i>&nbsp;{{ $t('common.save') }}
              </el-button>
              <el-button type="warning" @click="goBack">
                {{ $t('common.cancel') }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import SelectRooms from '@/components/room/selectRooms'
import { updateOwnerVote, listOwnerVote } from '@/api/oa/editOwnerVotingApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditOwnerVotingList',
  components: {
    SelectRooms
  },
  data() {
    return {
      editOwnerVotingInfo: {
        qaId: '',
        qaName: '',
        startTime: '',
        endTime: '',
        communityId: '',
        content: '',
        titleType: '',
        titleValues: [],
        roomIds: [],
        updateRoomIds: false,
        voteCount: 0
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.editOwnerVotingInfo.qaId = this.$route.query.qaId
    this.loadOwnerVoting()
  },
  methods: {
    async loadOwnerVoting() {
      try {
        const params = {
          page: 1,
          row: 1,
          qaId: this.editOwnerVotingInfo.qaId,
          communityId: this.communityId
        }
        const { data } = await listOwnerVote(params)
        Object.assign(this.editOwnerVotingInfo, data[0])
      } catch (error) {
        console.error('Failed to load voting data:', error)
      }
    },
    handleSelectRooms(selectRooms) {
      this.editOwnerVotingInfo.roomIds = selectRooms.map(item => item.roomId)
    },
    
    async saveOwnerVoting() {


      try {
        await updateOwnerVote(this.editOwnerVotingInfo)
        this.$message.success(this.$t('common.operationSuccess'))
        this.goBack()
      } catch (error) {
        this.$message.error(error.message || this.$t('editOwnerVoting.saveFailed'))
      }
    },
    changeAddTitleType() {
      const type = this.editOwnerVotingInfo.titleType
      if (type === '1001') {
        this.editOwnerVotingInfo.titleValues = [{ qaValue: '', seq: 1 }]
      } else if (type === '2002') {
        this.editOwnerVotingInfo.titleValues = [
          { qaValue: '', seq: 1 },
          { qaValue: '', seq: 2 }
        ]
      }
    },
    addTitleValue() {
      this.editOwnerVotingInfo.titleValues.push({
        qaValue: '',
        seq: this.editOwnerVotingInfo.titleValues.length + 1
      })
    },
    deleteTitleValue(seq) {
      this.editOwnerVotingInfo.titleValues = this.editOwnerVotingInfo.titleValues
        .filter(item => item.seq !== seq)
        .map((item, index) => ({ ...item, seq: index + 1 }))
    },
    updateRoomIdsMethod() {
      this.editOwnerVotingInfo.updateRoomIds = true
      this.$nextTick(() => {
        this.$refs.selectRooms.open()
      })
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.edit-owner-voting-container {
  padding: 20px;
}
</style>