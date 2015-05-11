# encoding: utf-8
require "logstash/outputs/base"
require "logstash/namespace"

class LogStash::Outputs::Cdaout < LogStash::Outputs::Base
    begin
        require "ap"
        rescue LoadError
    end

    config_name "cdaout"
    milestone 1
  
    default :codec, "line"

    config :silent, :validate => :boolean, :default => false

    public
    def register
        @perc_cnt = 0
        @perc_time = 0

        @codec.on_event do |event|
            unless @silent
                $stdout.write(event)
            end
        end
    end

    def receive(event)
        return unless output?(event)
        if event == LogStash::SHUTDOWN
            finished
            return
        end

        tags = event["tags"] 
        logmessage = event["logmessage"]

        if tags.include?("percolate")
            /\[Operation\:CDAPercolate\-(?<nat_id>\w*),\ count:\ (?<cnt>\d*)\, Total execution time\(ms\)\:\ (?<time_ms>\d*)/ =~ logmessage
            #puts "perc: #{nat_id} #{cnt} #{time_ms}"
            @perc_cnt += 1
            @perc_time += time_ms.to_i
        end

        @codec.encode(event) #codec_on_event triggered here

        if tags.include?("endfile")
            puts "perc cnt: #{@perc_cnt} time: #{@perc_time}"
        end
    end
end # class LogStash::Outputs::Stdout
