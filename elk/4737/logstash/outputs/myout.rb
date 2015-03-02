# encoding: utf-8
require "logstash/outputs/base"
require "logstash/namespace"

class LogStash::Outputs::Myout < LogStash::Outputs::Base
  begin
     require "ap"
  rescue LoadError
  end

  config_name "myout"
  milestone 1
  
  default :codec, "line"

  public
  def register
    @codec.on_event do |event|
      $stdout.write(event)
      #$stdout.puts("hello")
    end
  end

  def receive(event)
    return unless output?(event)
    if event == LogStash::SHUTDOWN
      finished
      return
    end
    @codec.encode(event) #codec_on_event triggered here
  end

end # class LogStash::Outputs::Stdout
