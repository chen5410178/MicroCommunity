<template>
  <div class="print-owner-voting-container">
    <div class="text-center">
      <h1>{{ printOwnerVotingInfo.qaName }}</h1>
    </div>

    <div class="text-left">
      <div v-html="printOwnerVotingInfo.content"></div>
    </div>

    <div>
      <table class="table vc-table-border margin-top" style="color:#000;font-size:14px;width: 100%;">
        <thead>
          <tr>
            <th class="text-center" >
              <div class="text-left">
                {{ $t('printOwnerVoting.room') }}
              </div>
            </th>
            <th
              v-for="(item, index) in printOwnerVotingInfo.titleValues"
              :key="'head-left-' + index"
              scope="col"
              class="text-center"
            >
              {{ item.qaValue }}
            </th>
            <th class="text-center ">
              <div class="text-left">
                {{ $t('printOwnerVoting.room') }}
              </div>
            </th>
            <th
              v-for="(item, index) in printOwnerVotingInfo.titleValues"
              :key="'head-right-' + index"
              scope="col"
              class="text-center"
            >
              {{ item.qaValue }}
            </th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(pair, index) in pairedVotes"
            :key="'row-' + index"
            class="vc-table-border"
          >
            <td class="text-center">
              <div style="max-width: 200px;">{{ pair.left ? pair.left.roomName : '' }}</div>
            </td>
            <td
              v-for="(item, tIndex) in printOwnerVotingInfo.titleValues"
              :key="'left-' + index + '-' + tIndex"
              class="text-center"
            >
              {{ pair.left ? pair.left[item.qaValue] : '' }}
            </td>
            <td class="text-center">
              <div style="max-width: 200px;">
                {{ pair.right ? pair.right.roomName : '' }}
              </div>
            </td>
            <td
              v-for="(item, tIndex) in printOwnerVotingInfo.titleValues"
              :key="'right-' + index + '-' + tIndex"
              class="text-center"
            >
              <div v-if="pair.right">
                {{ pair.right[item.qaValue] }}
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="summary text-left">
      <span>{{ $t('printOwnerVoting.totalVotes') }}：{{ printOwnerVotingInfo.voteCount }}</span>；
      <span>{{ $t('printOwnerVoting.votedCount') }}：{{ printOwnerVotingInfo.votedCount }}</span>；
      <span v-for="(item, tIndex) in printOwnerVotingInfo.titleValues" :key="'summary-' + tIndex">
        {{ item.qaValue }}: {{ item.personCount }}{{ $t('printOwnerVoting.person') }}；
      </span>
    </div>

    <div class="text-right margin-top margin-right">
      {{ currentCommunity.name }}
    </div>

    <div class="text-right margin-top-sm margin-right">
      {{ formatDate(new Date()) }}
    </div>

    <div id="print-btn" class="actions text-right">
   
      <button type="button" class="btn btn-warning float-right" style="margin-right:20px;" @click="_closePage">
        {{ $t('common.cancel') }}
      </button>
      <button class="btn btn-primary float-right" type="button" @click="_printPurchaseApplyDiv">
        <i class="fa fa-check"></i>&nbsp;{{ $t('common.print') }}
      </button>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listOwnerVote, listUserQuestionAnswer } from '@/api/oa/printOwnerVotingApi'

export default {
  name: 'PrintOwnerVotingList',
  data() {
    return {
      printOwnerVotingInfo: {
        qaName: '',
        content: '',
        titleValues: [],
        qaId: '',
        userVotes: [],
        voteCount: 0,
        votedCount: 0
      },
      currentCommunity: {
        name: '',
        communityId: ''
      }
    }
  },
  computed: {
    pairedVotes() {
      const votes = this.printOwnerVotingInfo.userVotes || []
      const result = []
      for (let i = 0; i < votes.length; i += 2) {
        result.push({
          left: votes[i] || null,
          right: votes[i + 1] || null
        })
      }
      return result
    }
  },
  created() {
    this.printOwnerVotingInfo.qaId = this.$route.query.qaId
    this.currentCommunity.communityId = getCommunityId()
    this._listOwnerVotings()
  },
  methods: {
    async _listOwnerVotings() {
      try {
        const params = {
          page: 1,
          row: 1,
          communityId: this.currentCommunity.communityId,
          qaId: this.printOwnerVotingInfo.qaId
        }
        const { data } = await listOwnerVote(params)
        //this.printOwnerVotingInfo = data[0]
        Object.assign(this.printOwnerVotingInfo, data[0])
        this._listValues()
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    async _listValues() {
      try {
        const params = {
          page: 1,
          row: 500,
          communityId: this.currentCommunity.communityId,
          qaId: this.printOwnerVotingInfo.qaId
        }
        const { data } = await listUserQuestionAnswer(params)
        const _titleValues = this.printOwnerVotingInfo.titleValues
        console.log(data, '_titleValues', _titleValues)
        data.forEach(_value => {
          _titleValues.forEach(_title => {
            _value[_title.qaValue] = ''
            if (_value['values']) {
              _value.values.forEach(tmpValue => {
                if (tmpValue.qaValue === _title.qaValue) {
                  _value[_title.qaValue] = 'V'
                }
              })
            }
          })
        })
        this.printOwnerVotingInfo.userVotes = data
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _printPurchaseApplyDiv() {
      document.getElementById("print-btn").style.display = "none"
      window.print()
      window.opener = null
      window.close()
    },
    _closePage() {
      window.opener = null
      window.close()
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.print-owner-voting-container {
  padding: 20px;

  .btn {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 6px 15px;
    border-radius: 4px;
    border: 1px solid transparent;
    cursor: pointer;
    font-size: 14px;
    line-height: 1.5;
    transition: all 0.2s;
  }

  .btn-primary {
    background-color: #409eff;
    border-color: #409eff;
    color: #fff;
  }

  .btn-warning {
    background-color: #e6a23c;
    border-color: #e6a23c;
    color: #fff;
  }

  .btn:hover {
    opacity: 0.85;
  }

  .margin-top {
    margin-top: 20px;
  }

  .margin-top-sm {
    margin-top: 10px;
  }

  .margin-right {
    margin-right: 20px;
  }

  .text-center {
    text-align: center;
  }

  .text-right {
    text-align: right;
  }

  .vc-table-border {
    border: 1px solid #000000;
    border-collapse: collapse;

    th,
    td {
      border: 1px solid #000000;
      padding: 8px;
    }
  }

  #print-btn {
    margin-top: 20px;
  }
}
</style>